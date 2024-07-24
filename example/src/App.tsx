import { useState, useEffect } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { extractContent } from 'react-native-pdf-text-extractor';

export default function App() {
  const [result, setResult] = useState<string | undefined>();

  useEffect(() => {
    extractContent('./assets/sample.txt').then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
