/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button
} from 'react-native';
import ZebraNet from 'react-native-zebra-link';

const onButt = () => {
   ZebraNet.findPrinters();
  //ZebraNet.printTcp("192.168.1.40", ZebraNet.ZPL_EXAMPLE_LABEL);
}

export default class App extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Button title="Press Me" onPress={onButt}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});
