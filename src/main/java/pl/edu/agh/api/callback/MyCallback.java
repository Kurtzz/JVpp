package pl.edu.agh.api.callback;

import io.fd.vpp.jvpp.VppCallbackException;
import io.fd.vpp.jvpp.core.callback.AfPacketCreateCallback;
import io.fd.vpp.jvpp.core.callback.AfPacketDeleteCallback;
import io.fd.vpp.jvpp.core.callback.ShowVersionCallback;
import io.fd.vpp.jvpp.core.dto.AfPacketCreateReply;
import io.fd.vpp.jvpp.core.dto.AfPacketDeleteReply;
import io.fd.vpp.jvpp.core.dto.ShowVersionReply;
import pl.edu.agh.api.ApiUtils;

public class MyCallback implements AfPacketCreateCallback, AfPacketDeleteCallback, ShowVersionCallback {
    @Override
    public void onAfPacketCreateReply(AfPacketCreateReply afPacketCreateReply) {
        System.out.printf("AfPacket create completed %s%n", afPacketCreateReply);
    }

    @Override
    public void onAfPacketDeleteReply(AfPacketDeleteReply afPacketDeleteReply) {
        System.out.printf("Af Packet Delete Reply: %s%n", afPacketDeleteReply);
    }

    @Override
    public void onError(VppCallbackException e) {
        System.out.printf("VPP Error %s%n", e);
    }

    @Override
    public void onShowVersionReply(ShowVersionReply showVersionReply) {
        try {
            System.out.println(ApiUtils.formatReply(showVersionReply));
        } catch (IllegalAccessException e) {
            System.err.println("JVPP ERROR: " + e);
        }
    }
}
