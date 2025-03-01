import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-pdf-text-extractor' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PdfTextExtractor = NativeModules?.PdfTextExtractor
  ? NativeModules?.PdfTextExtractor
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function extractContent(filePath: string): Promise<string> {
  return PdfTextExtractor.extractContent(filePath);
}
