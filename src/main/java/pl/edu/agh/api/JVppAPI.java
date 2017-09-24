package pl.edu.agh.api;

import io.fd.vpp.jvpp.VppInvocationException;
import io.fd.vpp.jvpp.core.dto.AfPacketCreate;
import io.fd.vpp.jvpp.core.dto.AfPacketCreateReply;
import io.fd.vpp.jvpp.core.dto.AfPacketDelete;
import io.fd.vpp.jvpp.core.dto.AfPacketDeleteReply;
import io.fd.vpp.jvpp.core.dto.IpFibDetailsReplyDump;
import io.fd.vpp.jvpp.core.dto.IpFibDump;
import io.fd.vpp.jvpp.core.dto.ShowVersion;
import io.fd.vpp.jvpp.core.dto.ShowVersionReply;
import io.fd.vpp.jvpp.core.dto.SwInterfaceAddDelAddress;
import io.fd.vpp.jvpp.core.dto.SwInterfaceAddDelAddressReply;
import io.fd.vpp.jvpp.core.dto.SwInterfaceDetailsReplyDump;
import io.fd.vpp.jvpp.core.dto.SwInterfaceDump;
import io.fd.vpp.jvpp.core.dto.SwInterfaceSetFlags;
import io.fd.vpp.jvpp.core.dto.SwInterfaceSetFlagsReply;
import io.fd.vpp.jvpp.core.future.FutureJVppCoreFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static pl.edu.agh.Consts.INTERFACE_NAME;

@Service("api")
public class JVppAPI {

    @Autowired
    private FutureJVppCoreFacade futureJVppCoreFacade;

    // vpp# delete host-interface name <name>
    public AfPacketDeleteReply deleteHostInterface() throws Exception {
        AfPacketDelete afPacketDelete = new AfPacketDelete();
        afPacketDelete.hostIfName = INTERFACE_NAME.getBytes();

        Future<AfPacketDeleteReply> future = futureJVppCoreFacade.afPacketDelete(afPacketDelete).toCompletableFuture();
        return future.get();
    }

    // vpp# create host-interface name <name>
    public AfPacketCreateReply createHostInterface(String name) throws Exception {
        AfPacketCreate afPacketCreate = new AfPacketCreate();
        afPacketCreate.hostIfName = name.getBytes();
        afPacketCreate.useRandomHwAddr = 1;

        Future<AfPacketCreateReply> future = futureJVppCoreFacade.afPacketCreate(afPacketCreate).toCompletableFuture();

        return future.get();
    }

    // vpp# set int state <interface> up
    public SwInterfaceSetFlagsReply SetIntStateUpDown(int interfaceIndex, boolean isUp) throws VppInvocationException, InterruptedException, ExecutionException {
        SwInterfaceSetFlags swInterfaceSetFlags = new SwInterfaceSetFlags();

        swInterfaceSetFlags.adminUpDown = (byte) (isUp ? 1 : 0);
        swInterfaceSetFlags.deleted = 0;
        swInterfaceSetFlags.linkUpDown = 0;
        swInterfaceSetFlags.swIfIndex = interfaceIndex;

        Future<SwInterfaceSetFlagsReply> future = futureJVppCoreFacade.swInterfaceSetFlags(swInterfaceSetFlags).toCompletableFuture();
        return future.get();
    }

    // vpp# show int
    public SwInterfaceDetailsReplyDump showInterfaces() throws VppInvocationException, ExecutionException, InterruptedException {
        Future<SwInterfaceDetailsReplyDump> future = futureJVppCoreFacade.swInterfaceDump(new SwInterfaceDump()).toCompletableFuture();
        return future.get();
    }

    // vpp# set int ip address <interface> <address>/<prefix>
    public SwInterfaceAddDelAddressReply setIntIpAddress(int intIndex, byte[] address, byte addressLength, boolean isIpv6, boolean isAdd, boolean delAll) throws ExecutionException, InterruptedException {
        SwInterfaceAddDelAddress request = new SwInterfaceAddDelAddress();
        request.swIfIndex = intIndex;
        request.address = address;
        request.addressLength = addressLength;

        request.isIpv6 = (byte) (isIpv6 ? 1 : 0);
        request.isAdd = (byte) (isAdd ? 1 : 0);
        request.delAll = (byte) (delAll ? 1 : 0);

        Future<SwInterfaceAddDelAddressReply> future = futureJVppCoreFacade.swInterfaceAddDelAddress(request).toCompletableFuture();
        return future.get();
    }

    public ShowVersionReply showVersion() throws Exception {
        Future<ShowVersionReply> future = futureJVppCoreFacade.showVersion(new ShowVersion()).toCompletableFuture();
        return future.get();
    }

    // vpp# show ip fib
    public IpFibDetailsReplyDump showIpFib() throws InterruptedException, VppInvocationException, ExecutionException, UnknownHostException, IllegalAccessException {
        IpFibDump dump = new IpFibDump();
        Future<IpFibDetailsReplyDump> future = futureJVppCoreFacade.ipFibDump(dump).toCompletableFuture();

        return future.get();
    }
}
