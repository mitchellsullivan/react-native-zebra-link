package net.mitchellsullivan.rnzebralink;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterNetwork;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;
import com.zebra.sdk.printer.discovery.DiscoveryException;
import com.zebra.sdk.printer.discovery.NetworkDiscoverer;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;


public class ZebraNet extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String ZPL_TEST = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";
    private static final String ZPL_EXAMPLE_LABEL =
            "^XA\n" +
            "^LT120\n" +
            "^FX Top section\n" +
            "^CFB,25\n" +
            "^FO50,173^FDFROM:^FS\n" +
            "^FO200,173^FDTest sender^FS\n" +
            "^FO200,228^FD10 MOUNTAIN PKWY^FS\n" +
            "^FO200,283^FDTN, COLLIERVILLE, 38017^FS\n" +
            "^FO50,343^GB706,1,3^FS\n" +
            "^FX Second section with recipient address\n" +
            "^CFB,25\n" +
            "^FO50,363^FDTO:^FS\n" +
            "^FO200,363^FDJohn Smith^FS\n" +
            "^FO200,423^FDAccounts Payable Dept.^FS\n" +
            "^FO200,473^FD123 Market Street^FS\n" +
            "^FO200,523^FDTX, Dallas, 75270^FS\n" +
            "^FO50,4830^GB706,1,3^FS\n" +
            "^FX Third section with shipment numbers\n" +
            "^CFB,25\n" +
            "^FO60,593^FDPO#^FS\n" +
            "^FO230,593^FB542,1,0,N,0^FD0001234^FS\n" +
            "^FO60,623^FDDept.^FS\n" +
            "^FO230,653^FB542,1,0,N,0^FD^FS\n" +
            "^FO60,723^FDStore^FS\n" +
            "^FO230,723^FB542,1,0,N,0^FDMAIN BRANCH^FS\n" +
            "^FO60,823^FDDuns#^FS\n" +
            "^FO230,823^FB542,1,0,N,0^FD123123123^FS\n" +
            "^FO50,873^GB706,1,3^FS\n" +
            "^FX Fourth section with package description\n" +
            "^CFB,25\n" +
            "^FO60,893^FB692,5,4,N,0^FDPRODUCT A x 1, PRODUCT B x 4\\&^FS\n" +
            "^FX Fifth section with Box counter\n" +
            "^CFB,25\n" +
            "^FO60,1000^FB692,1,0,C,0^FDBox 1 of 1^FS\n" +
            "^FO60,1000^FB692,1,0,C,0^FD__________^FS\n" +
            "^FO60,1000^FB692,1,0,C,0^FD__________^FS\n" +
            "^XZ";

    public ZebraNet(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ZebraNet";
    }

    @ReactMethod
    public void show(String message, int duration) {
      Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @Override
    public Map<String, Object> getConstants() {
      final Map<String, Object> constants = new HashMap<>();
      constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
      constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
      constants.put("ZPL_TEST", ZPL_TEST);
      constants.put("ZPL_EXAMPLE_LABEL", ZPL_EXAMPLE_LABEL);
      return constants;
    }

    private DiscoveryHandler discoveryHandler = new DiscoveryHandler() {
        List<DiscoveredPrinter> printers = new ArrayList<DiscoveredPrinter>();

        public void foundPrinter(DiscoveredPrinter printer) {
            printers.add(printer);
        }

        public void discoveryFinished() {
            for (DiscoveredPrinter printer : printers) {
                System.out.println(printer);
            }
            System.out.println("Discovered " + printers.size() + " printers.");
        }

        public void discoveryError(String message) {
            System.out.println("An error occurred during discovery : " + message);
        }
    };

    @ReactMethod
    public void findPrinters() {
        try {
            System.out.println("Starting printer discovery.");
            NetworkDiscoverer.findPrinters(discoveryHandler);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void printTcp(String ip, String text) {
        try {
            sendZplOverTcp(ip, text);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void sendZplOverTcp(String ip, String text) throws ConnectionException {
        Connection conn = new TcpConnection(ip, TcpConnection.DEFAULT_ZPL_TCP_PORT);
        try {
            conn.open();
            if (text == null || text.trim().isEmpty()) {
                text = ZPL_TEST;
            }
            conn.write(text.getBytes());
        } catch (ConnectionException e) {
            System.err.println(e.getMessage());
        } finally {
            conn.close();
        }
    }
}