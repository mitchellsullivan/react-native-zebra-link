package net.mitchellsullivan.rctzebralink;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
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


public class ZebraLink extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String ZPL_TEST = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

    public ZebraLink(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ZebraLink";
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
      return constants;
    }

    public class DiscoveryReturner implements DiscoveryHandler {
        Callback callback;
        List<DiscoveredPrinter> printers = new ArrayList<DiscoveredPrinter>();
        WritableArray resPrinters = new WritableNativeArray();

        DiscoveryReturner(Callback cb) {
            super();
            callback = cb;
        }

        public void foundPrinter(DiscoveredPrinter printer) {
            printers.add(printer);
        }

        public void discoveryFinished() {
            for (DiscoveredPrinter printer : printers) {
                //System.out.println(printer);
                resPrinters.pushString(printer.address);
            }
            //System.out.println("Discovered " + printers.size() + " printers.");
            callback.invoke(null, resPrinters);
        }

        public void discoveryError(String message) {
            System.out.println("An error occurred during discovery : " + message);
        }
    }

    @ReactMethod
    public void findPrinters(Callback cb) {
        try {
            DiscoveryReturner discoveryHandler = new DiscoveryReturner(cb);
            NetworkDiscoverer.findPrinters(discoveryHandler);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void printTcp(String ip, int port, String text) {
        try {
            sendZplOverTcp(ip, port, text);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void sendZplOverTcp(String ip, int port, String text) throws ConnectionException {
        // Connection conn = new TcpConnection(ip, TcpConnection.DEFAULT_ZPL_TCP_PORT);
        Connection conn = new TcpConnection(ip, port);
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