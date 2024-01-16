import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-jwt' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Jwt = NativeModules.Jwt
  ? NativeModules.Jwt
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function sign(
  header: Record<string, unknown>,
  payload: Record<string, unknown>,
  privateKey: string
): Promise<string | null> {
  return Jwt.sign(header, payload, privateKey);
}
