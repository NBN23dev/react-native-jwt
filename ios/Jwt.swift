import Foundation
import SwiftJWT

struct Payload: Claims {
  let iss: String
  let sub: String
  let aud: String
  let iat: Int64
  let exp: Int64
}

@objc(Jwt) 
class JWTModule: NSObject {  
  private func _sign(header: [String: Any], payload: [String: Any], privateKey: String) -> String? {
    let signer = JWTSigner.rs256(privateKey: privateKey.data(using: .utf8)!)
    
    let typ = header["typ"] as? String ?? "JWT"
    let kid = header["kid"] as? String ?? ""
    
    let header = Header(typ: typ, kid: kid)
    
    let iss = payload["iss"] as? String ?? ""
    let sub = payload["sub"] as? String ?? ""
    let aud = payload["aud"] as? String ?? ""
    let iat = payload["iat"] as? Int64 ?? 0
    let exp = payload["exp"] as? Int64 ?? 0
    
    let claims = Payload(iss: iss, sub: sub, aud: aud, iat: iat, exp: exp)
    
    var jwt = JWT(header: header, claims: claims)
    
    return try? jwt.sign(using: signer)
  }
  
  @objc func sign(
    _ header: [String: Any],
    payload: [String: Any],
    privateKey: String,
    resolver resolve: RCTPromiseResolveBlock,
    rejecter reject: RCTPromiseRejectBlock
  ) -> Void {    
    let token = _sign(header: header, payload: payload, privateKey: privateKey)
        
    resolve(token)
  }
}
