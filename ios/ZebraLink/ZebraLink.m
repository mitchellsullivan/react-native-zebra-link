
#import "ZebraLink.h"
#import "ZebraPrinterFactory.h"
#import "TcpPrinterConnection.h"
#import "ZebraPrinterConnection.h"
#import "NetworkDiscoverer.h"
#import "DiscoveredPrinter.h"

@implementation ZebraLink

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(printTcp:(NSString*)ipAddress port:(int)port text:(NSString*)text) {
    
    id<ZebraPrinterConnection, NSObject> conn =
        [[TcpPrinterConnection alloc] initWithAddress:ipAddress
                                          andWithPort:port];
    if ([conn open]) {
        [conn write:[text dataUsingEncoding:NSUTF8StringEncoding] error:nil];
    }
    [conn close];
}


RCT_EXPORT_METHOD(findPrinters:(RCTResponseSenderBlock)callback) {
    NSMutableArray *objs = [[NSMutableArray alloc]init];
    for(DiscoveredPrinter *d in [NetworkDiscoverer localBroadcast:nil]) {
        [objs addObject:[NSString stringWithFormat:@"%@", d.address]];
    }
    callback(@[[NSNull null], objs]);
}

@end


