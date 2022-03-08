import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Button
} from 'react-native';
import ZebraLink from 'react-native-zebra-link';

const sample = `^XA
    ^LT120
    ^FX Top section
    ^CFB,25
    ^FO50,173^FDFROM:^FS
    ^FO200,173^FDTest sender^FS
    ^FO200,228^FD10 MOUNTAIN PKWY^FS
    ^FO200,283^FDTN, COLLIERVILLE, 38017^FS
    ^FO50,343^GB706,1,3^FS
    ^FX Second section with recipient address
    ^CFB,25
    ^FO50,363^FDTO:^FS
    ^FO200,363^FDJohn Smith^FS
    ^FO200,423^FDAccounts Payable Dept.^FS
    ^FO200,473^FD123 Market Street^FS
    ^FO200,523^FDTX, Dallas, 75270^FS
    ^FO50,4830^GB706,1,3^FS
    ^FX Third section with shipment numbers
    ^CFB,25
    ^FO60,593^FDPO#^FS
    ^FO230,593^FB542,1,0,N,0^FD0001234^FS
    ^FO60,623^FDDept.^FS
    ^FO230,653^FB542,1,0,N,0^FD^FS
    ^FO60,723^FDStore^FS
    ^FO230,723^FB542,1,0,N,0^FDMAIN BRANCH^FS
    ^FO60,823^FDDuns#^FS
    ^FO230,823^FB542,1,0,N,0^FD123123123^FS
    ^FO50,873^GB706,1,3^FS
    ^FX Fourth section with package description
    ^CFB,25
    ^FO60,893^FB692,5,4,N,0^FDPRODUCT A x 1, PRODUCT B x 4\&^FS
    ^FX Fifth section with Box counter
    ^CFB,25
    ^FO60,1000^FB692,1,0,C,0^FDBox 1 of 1^FS
    ^FO60,1000^FB692,1,0,C,0^FD__________^FS
    ^FO60,1000^FB692,1,0,C,0^FD__________^FS
    ^XZ`


let ip = "";

const onPressLogButton = () => {
  ZebraLink.findPrinters((err, printerIps) => {
    console.log(printerIps[0]);
    ip = printerIps[0];
  });
}

const onPressPrintButton = () => {
  ZebraLink.printTcp(ip, 9100, sample);
}

export default class App extends Component {
 render() {
   return (
     <View style={styles.container}>
       <Button title="Log Printers" onPress={onPressLogButton}/>
       <Button title="Print over TCP" onPress={onPressPrintButton}/>
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
