/****************************************************************************
 Copyright (c) 2013 cocos2d-x.org
 
 http://www.cocos2d-x.org
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#import "IOSIAP.h"
#define OUTPUT_LOG(...)     if (_debug) NSLog(__VA_ARGS__);

@implementation IOSIAP

BOOL _debug;
NSString *_productId;
NSString *_notifyURL;
NSString *_rechargeOrderNo;
NSArray *_productArray;
//productsRequest;
SKProductsRequest *_productsRequest;
NSMutableDictionary *_uncommitOrders;

- (void) configDeveloperInfo: (NSMutableDictionary*) devInfo
{
    [self loadUncommitOrders];
    [self recommitOrders];
    [[SKPaymentQueue defaultQueue] addTransactionObserver:self];
}

- (void) payForProduct: (NSMutableDictionary*) cpInfo{
    if ([SKPaymentQueue canMakePayments] == NO) {
        return;
    }
    
    NSString* productIds;
    if ([cpInfo objectForKey:@"ProductId"]) {
        _productId = [cpInfo objectForKey:@"ProductId"];
    }
    else {
        _productId = [cpInfo objectForKey:@"product_id"];
    }
    if ([cpInfo objectForKey:@"ProductIds"]) {
        productIds = [cpInfo objectForKey:@"ProductIds"];
    }
    else {
        productIds = _productId;
    }
    _notifyURL = [cpInfo objectForKey:@"NotifyUrl"];
    _rechargeOrderNo = [cpInfo objectForKey:@"RechargeOrderNo"];
    SKProduct *skProduct = [self getProductById:_productId];
    if (skProduct) {
        SKMutablePayment *payment = [SKMutablePayment paymentWithProduct:skProduct];
        [[SKPaymentQueue defaultQueue] addPayment:payment];
        OUTPUT_LOG(@"add PaymentQueue");
    }
    else {
        [self requestProducts:productIds];
    }
}

- (void) setDebugMode: (BOOL) debug{
    _debug = debug;
}
- (NSString*) getSDKVersion{
    return @"10.0";
}

- (NSString*) getPluginVersion{
    return @"1.0";
}

- (NSString*) getPluginName{
    return @"iOS";
}

- (void) requestProducts:(NSString*) paramMap {
    NSArray *producIdArray = [paramMap componentsSeparatedByString:@","];
    NSSet *productIdentifiers = [NSSet setWithArray:producIdArray];
    OUTPUT_LOG(@"param is %@", productIdentifiers);
    _productsRequest = [[SKProductsRequest alloc] initWithProductIdentifiers:productIdentifiers];
    _productsRequest.delegate = self;
    [_productsRequest start];
}

-(SKProduct *)getProductById:(NSString *)productid{
    for (SKProduct * skProduct in _productArray) {
        if([skProduct.productIdentifier isEqualToString:productid]){
            return skProduct;
        }
    }
    return NULL;
}

- (void)request:(SKRequest *)request didFailWithError:(NSError *)error {
    OUTPUT_LOG(@"Failed to load list of products.");
     [IAPWrapper onRequestProduct:self withRet:RequestFail withProducts:NULL];
    _productsRequest = nil;
}

//SKProductsRequestDelegate needed
- (void) productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response{
    _productArray = response.products;
    for (SKProduct * skProduct in _productArray) {
        OUTPUT_LOG(@"Found product: %@ %@ %0.2f",
              skProduct.productIdentifier,
              skProduct.localizedTitle,
              skProduct.price.floatValue);
    }
    [IAPWrapper onRequestProduct:self withRet:RequestSuccees withProducts:_productArray];
    _productsRequest = nil;
    
    if (_productId != nil) {
        SKProduct *skProduct = [self getProductById:_productId];
        if (skProduct) {
            SKMutablePayment *payment = [SKMutablePayment paymentWithProduct:skProduct];
            [[SKPaymentQueue defaultQueue] addPayment:payment];
            OUTPUT_LOG(@"add PaymentQueue");
        }
    }
}

//SKPaymentTransactionObserver needed
- (void) paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions{
    for (SKPaymentTransaction * transaction in transactions) {
        switch (transaction.transactionState)
        {
            case SKPaymentTransactionStatePurchased:
                [self completeTransaction:transaction];
                break;
            case SKPaymentTransactionStateFailed:
                [self failedTransaction:transaction];
                break;
            case SKPaymentTransactionStateRestored:
                [self restoreTransaction:transaction];
            default:
                break;
        }
    };
}

- (void) completeTransaction:(SKPaymentTransaction *)transaction {
    NSString *receipt = nil;
    if (_notifyURL != nil && _rechargeOrderNo != nil) {
        if (floor(NSFoundationVersionNumber) <= NSFoundationVersionNumber_iOS_6_1) {
            // iOS 6.1 or earlier.
            // Use SKPaymentTransaction's transactionReceipt.
            receipt = [self encode:(uint8_t *)transaction.transactionReceipt.bytes length:transaction.transactionReceipt.length];
            
        }
        else {
            // iOS 7 or later.
            NSURL *receiptURL = [[NSBundle mainBundle] appStoreReceiptURL];
            NSData *recData = [NSData dataWithContentsOfURL:receiptURL];
            receipt = [recData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
            if (!receipt) {
                receipt = [self encode:(uint8_t *)transaction.transactionReceipt.bytes length:transaction.transactionReceipt.length];
            }
        }
        
        if (receipt != nil) {
            NSMutableDictionary *orderInfo = [[NSMutableDictionary alloc] initWithObjects:@[_rechargeOrderNo,_notifyURL,receipt] forKeys:@[@"RechargeOrderNo",@"NotifyUrl",@"Receipt"]];
            [_uncommitOrders setObject:orderInfo forKey:_rechargeOrderNo];
            [self saveUncommitOrders];
            
            NSURLSession *session = [NSURLSession sharedSession];
            NSURL *url = [NSURL URLWithString:_notifyURL];
            
            // 创建一个请求对象，并这是请求方法为POST，把参数放在请求体中传递
            NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
            request.HTTPMethod = @"POST";
            NSCharacterSet *URLBase64CharacterSet = [[NSCharacterSet characterSetWithCharactersInString:@"/+=\n"] invertedSet];
            request.HTTPBody = [[NSString stringWithFormat:@"RechargeOrderNo=%@&Receipt=%@", _rechargeOrderNo, [receipt stringByAddingPercentEncodingWithAllowedCharacters:URLBase64CharacterSet]] dataUsingEncoding:NSUTF8StringEncoding];
            
            NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request completionHandler:^(NSData * __nullable data, NSURLResponse * __nullable response, NSError * __nullable error) {
                // 拿到响应头信息
                NSHTTPURLResponse *res = (NSHTTPURLResponse *)response;
                if ([res statusCode] == 200) {
                    NSError *error;
                    NSDictionary *result = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
                    if (result != nil && [result objectForKey:@"RechargeOrderNo"]) {
                        [_uncommitOrders removeObjectForKey:[result objectForKey:@"RechargeOrderNo"]];
                        [self saveUncommitOrders];
                    }
                }
            }];
            [dataTask resume];
        }
    }

    
    [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
    [IAPWrapper onPayResult:self withRet:PaymentTransactionStatePurchased withMsg:@""];
}

- (void) restoreTransaction:(SKPaymentTransaction *)transaction {
    OUTPUT_LOG(@"restoreTransaction...");
    [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
    [IAPWrapper onPayResult:self withRet:PaymentTransactionStateRestored withMsg:@""];
}

- (void) failedTransaction:(SKPaymentTransaction *)transaction {
    OUTPUT_LOG(@"failedTransaction...");
    if (transaction.error.code != SKErrorPaymentCancelled)
    {
        OUTPUT_LOG(@"Transaction error: %@", transaction.error.localizedDescription);
        [[[UIAlertView alloc] initWithTitle:@"支付结果" message:transaction.error.localizedDescription delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }
    
    [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
    [IAPWrapper onPayResult:self withRet:PaymentTransactionStateFailed withMsg:@""];
}

- (void) restoreCompletedTransactions {
    [[SKPaymentQueue defaultQueue] restoreCompletedTransactions];
}

- (NSString *) encode:(const uint8_t *)input length:(NSInteger)length
{
    static char table[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    NSMutableData *data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t *output = (uint8_t *)data.mutableBytes;
    
    for (NSInteger i = 0; i < length; i += 3) {
        NSInteger value = 0;
        for (NSInteger j = i; j < (i + 3); j++) {
            value <<= 8;
            
            if (j < length) {
                value |= (0xFF & input[j]);
            }
        }
        
        NSInteger index = (i / 3) * 4;
        output[index + 0] =                    table[(value >> 18) & 0x3F];
        output[index + 1] =                    table[(value >> 12) & 0x3F];
        output[index + 2] = (i + 1) < length ? table[(value >> 6)  & 0x3F] : '=';
        output[index + 3] = (i + 2) < length ? table[(value >> 0)  & 0x3F] : '=';
    }
    
    return [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding] ;
}

- (void) loadUncommitOrders
{
    _uncommitOrders = [NSMutableDictionary dictionaryWithCapacity:10];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDir = [paths objectAtIndex:0];
    NSString *filePath = [documentDir stringByAppendingPathComponent:@"plugins/ios/UncommitOrders.json"];
    NSData *data = [[NSData alloc] initWithContentsOfFile:filePath];
    
    if (data != nil) {
        NSError *error = nil;
        NSDictionary *jsonObject = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        [_uncommitOrders setDictionary:jsonObject];
    }
}

- (void) saveUncommitOrders
{
    if (_uncommitOrders == nil) {
        return;
    }
    
    if ([NSJSONSerialization isValidJSONObject:_uncommitOrders])
    {
        NSError *error;
        NSData *data = [NSJSONSerialization dataWithJSONObject:_uncommitOrders options:NSJSONWritingPrettyPrinted error:&error];
        if (data != nil) {
            NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
            NSString *documentDir = [paths objectAtIndex:0];
            NSString *fileDir = [documentDir stringByAppendingPathComponent:@"plugins/ios/"];
            NSString *filePath = [fileDir stringByAppendingPathComponent:@"UncommitOrders.json"];
            
            [[NSFileManager defaultManager] createDirectoryAtPath:fileDir withIntermediateDirectories:YES attributes:nil error:&error];
            [data writeToFile:filePath atomically:YES];
        }
    }
}

- (void) recommitOrders
{
    BOOL resave = NO;
    NSURLSession *session = [NSURLSession sharedSession];
    NSEnumerator * enumeratorValue = [_uncommitOrders objectEnumerator];
    for (NSMutableDictionary *object in enumeratorValue) {
        NSString *rechargeOrderNo = [object objectForKey:@"RechargeOrderNo"];
        NSString *notifyURL = [object objectForKey:@"NotifyUrl"];
        NSString *receipt = [object objectForKey:@"Receipt"];
        
        if (rechargeOrderNo == nil || notifyURL == nil || receipt == nil) {
            [_uncommitOrders removeObjectForKey:rechargeOrderNo];
            resave = YES;
            continue;
        }
        
        NSURL *url = [NSURL URLWithString:notifyURL];
        
        // 创建一个请求对象，并这是请求方法为POST，把参数放在请求体中传递
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
        request.HTTPMethod = @"POST";
        NSCharacterSet *URLBase64CharacterSet = [[NSCharacterSet characterSetWithCharactersInString:@"/+=\n"] invertedSet];
        request.HTTPBody = [[NSString stringWithFormat:@"RechargeOrderNo=%@&Receipt=%@", rechargeOrderNo, [receipt stringByAddingPercentEncodingWithAllowedCharacters:URLBase64CharacterSet]] dataUsingEncoding:NSUTF8StringEncoding];
        
        NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request completionHandler:^(NSData * __nullable data, NSURLResponse * __nullable response, NSError * __nullable error) {
            // 拿到响应头信息
            NSHTTPURLResponse *res = (NSHTTPURLResponse *)response;
            if ([res statusCode] == 200) {
                NSError *error;
                NSDictionary *result = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
                if (result != nil && [result objectForKey:@"RechargeOrderNo"]) {
                    [_uncommitOrders removeObjectForKey:[result objectForKey:@"RechargeOrderNo"]];
                    [self saveUncommitOrders];
                }
            }
        }];
        [dataTask resume];
    }
    
    if (resave) {
        [self saveUncommitOrders];
    }
}
@end
