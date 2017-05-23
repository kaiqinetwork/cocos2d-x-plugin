/****************************************************************************
Copyright (c) 2012+2013 cocos2d+x.org

http://www.cocos2d+x.org

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

#import <Foundation/Foundation.h>

typedef enum {
    kInitSuccess = 0,/**< enum value is callback of succeeding in initing sdk. */
    kInitFail,/**< enum  value is callback of failing to init sdk. */
    kLoginSuccess,/**< enum value is callback of succeeding in login.*/
    kLoginNetworkError,/**< enum value is callback of network error*/
    kLoginNoNeed,/**< enum value is callback of no need login.*/
    kLoginFail,/**< enum value is callback of failing to login. */
    kLoginCancel,/**< enum value is callback of canceling to login. */
    kLogoutSuccess,/**< enum value is callback of succeeding in logout. */
    kLogoutFail,/**< enum value is callback of failing to logout. */
    kPlatformEnter,/**< enum value is callback after enter platform. */
    kPlatformBack,/**< enum value is callback after exit antiAddiction. */
    kPausePage,/**< enum value is callback after exit pause page. */
    kExitPage,/**< enum value is callback after exit exit page. */
    kAntiAddictionQuery,/**< enum value is callback after querying antiAddiction. */
    kRealNameRegister,/**< enum value is callback after registering realname. */
    kAccountSwitchSuccess,/**< enum value is callback of succeeding in switching account. */
    kAccountSwitchFail,/**< enum value is callback of failing to switch account. */
    kOpenShop,/**< enum value is callback of open the shop. */
    kUserExtension = 50000 /**< enum value is  extension code . */
} UserActionResult;

@interface UserWrapper : NSObject
{
    
}

+ (void) onActionResult:(id) obj withRet:(UserActionResult) ret withMsg:(NSString*) msg;
@end
