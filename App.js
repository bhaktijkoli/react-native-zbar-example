/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import { launchCamera } from 'react-native-image-picker'
import { NativeModules } from 'react-native';
const { ZBarModule } = NativeModules;

import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Button,
  Text,
  View,
} from 'react-native';
import { useState } from 'react';
import Camera from './Camera';

const App = () => {
  const [isRNCameraActive, setRNCameraActive] = useState(false);

  const handleRNCamera = () => {
    setRNCameraActive(true)
  }

  const handleZBarCamera = () => {
    launchCamera(
      {
        selectionLimit: 1,
        mediaType: 'photo',
      },
      imgRes => {
        if (imgRes.assets && imgRes.assets?.length > 0) {
          const { uri, fileName, type } = imgRes.assets[0]
          console.log({ uri, fileName, type })
          if (
            uri === undefined ||
            fileName === undefined ||
            type === undefined
          ) {
            // TODO: Size Check
            // TODO: Show error msg
            return
          }
          ZBarModule.scanImageFromURL(uri)
            .then((result) => {
              console.log({ result });
            })
            .catch((err) => {
              console.log(err)
            })
        }
      },
    );
  }
  return (
    <SafeAreaView style={styles.background}>
      <StatusBar barStyle={'dark-content'} />
      {
        isRNCameraActive ? <Camera /> : (
          <View
            style={[styles.background, styles.container]}>
            <Button
              style={styles.button}
              onPress={handleRNCamera}
              title="Open RN Camera"
              color="#841584"
            />
            <Button
              style={styles.button}
              onPress={handleZBarCamera}
              title="Open ZBar Camera"
              color="#841584"
            />
          </View>
        )
      }
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  background: {
    backgroundColor: '#FFF',
    flex: 1,
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center'
  },
  button: {
    marginBottom: 10,
  }
});

export default App;