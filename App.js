/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Button,
  Text,
  View,
} from 'react-native';

const App = () => {
  const onPressScan = () => {
    alert("Hello")
  }
  return (
    <SafeAreaView style={styles.background}>
      <StatusBar barStyle={'dark-content'} />
      <View
        style={[styles.background, styles.container]}>
        <Button
          style={styles.button}
          onPress={onPressScan}
          title="Scan"
          color="#841584"
        />
      </View>
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
    width: '100px',
    backgroundColor: 'red',
  }
});

export default App;