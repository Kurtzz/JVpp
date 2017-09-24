package pl.edu.agh;

import io.fd.vpp.jvpp.JVppRegistry;
import io.fd.vpp.jvpp.core.dto.AfPacketCreateReply;
import io.fd.vpp.jvpp.core.dto.AfPacketDeleteReply;
import io.fd.vpp.jvpp.core.dto.IpFibDetails;
import io.fd.vpp.jvpp.core.dto.IpFibDetailsReplyDump;
import io.fd.vpp.jvpp.core.dto.SwInterfaceAddDelAddressReply;
import io.fd.vpp.jvpp.core.dto.SwInterfaceDetails;
import io.fd.vpp.jvpp.core.dto.SwInterfaceDetailsReplyDump;
import io.fd.vpp.jvpp.core.dto.SwInterfaceSetFlagsReply;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.agh.api.ApiUtils;
import pl.edu.agh.api.JVppAPI;
import pl.edu.agh.config.AppConfig;

public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        JVppAPI api = context.getBean("api", JVppAPI.class);

        //SHOW VERSION
        System.out.println(ApiUtils.formatReply(api.showVersion()));

        //CREATE HOST INTERFACE
        AfPacketCreateReply createReply = api.createHostInterface(Consts.INTERFACE_NAME);
        System.out.println(ApiUtils.formatReply(createReply));

        //SET INTERFACE STATE UP
        SwInterfaceSetFlagsReply setReply = api.SetIntStateUpDown(createReply.swIfIndex, true);
//        System.out.println(ApiUtils.formatReply(setReply));

        //SET INTERFACE IP ADDRESS
        SwInterfaceAddDelAddressReply ipAddresReply = api.setIntIpAddress(createReply.swIfIndex, new byte[]{10, 10, 1, 2}, (byte) 24, false, true, false);
//        System.out.println(ApiUtils.formatReply(ipAddresReply));

        //SHOW INTERFACES
        SwInterfaceDetailsReplyDump intReply = api.showInterfaces();
        for (SwInterfaceDetails swInterfaceDetails : intReply.swInterfaceDetails) {
            System.out.println(ApiUtils.formatReply(swInterfaceDetails));
        }

        //SHOW IP FIB (ROUTING TABLE)
        IpFibDetailsReplyDump ipFibReply = api.showIpFib();
        for (IpFibDetails ipFibDetails : ipFibReply.ipFibDetails) {
            System.out.println(ApiUtils.formatReply(ipFibDetails));
        }

        //DELETE HOST INTERFACE
        AfPacketDeleteReply deleteReply = api.deleteHostInterface();
        System.out.println(ApiUtils.formatReply(deleteReply));

        context.getBean("registry", JVppRegistry.class).close();
    }
}
