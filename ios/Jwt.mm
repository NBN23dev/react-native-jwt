#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(Jwt, NSObject)

RCT_EXTERN_METHOD(
  sign:(NSDictionary *)header
  payload:(NSDictionary *)payload
  privateKey:(NSString *)privateKey
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
