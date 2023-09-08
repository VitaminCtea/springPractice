package spring;

class TCPOctetStream {}

class TCPState {
    public void transmit(TCPConnection connection, TCPOctetStream stream) {}
    public void activeOpen(TCPConnection connection) {}
    public void passiveOpen(TCPConnection connection) {}
    public void close(TCPConnection connection) {}
    public void send(TCPConnection connection) {}
    public void acknowledge(TCPConnection connection) {}
    public void synchronize(TCPConnection connection) {}
    protected void changeState(TCPConnection connection, TCPState tcpState) { connection.changeState(tcpState); }
    @Override public String toString() { return this.getClass().getSimpleName(); }
}

class TCPEstablished extends TCPState {
    private static class Instance { private static final TCPState instance = new TCPEstablished(); }
    public static TCPState instance() { return Instance.instance; }

    @Override public void transmit(TCPConnection connection, TCPOctetStream stream) { connection.processOctet(stream); }
    @Override public void close(TCPConnection connection) { changeState(connection, TCPListen.instance()); }
}

class TCPListen extends TCPState {
    private static class Instance { private static final TCPState instance = new TCPListen(); }
    public static TCPState instance() { return Instance.instance; }

    @Override public void send(TCPConnection connection) { changeState(connection, TCPClosed.instance()); }
}

class TCPClosed extends TCPState {
    private static class Instance { private static final TCPState instance = new TCPClosed(); }
    public static TCPState instance() { return Instance.instance; }

    @Override public void activeOpen(TCPConnection connection) { changeState(connection, TCPEstablished.instance()); }
    @Override public void passiveOpen(TCPConnection connection) { changeState(connection, TCPListen.instance()); }
}

class TCPConnection {
    private TCPState tcpState;

    public TCPConnection() { tcpState = TCPClosed.instance(); }

    public void activeOpen() { tcpState.activeOpen(this); }
    public void passiveOpen() { tcpState.passiveOpen(this); }
    public void close() { tcpState.close(this); }
    public void acknowledge() { tcpState.acknowledge(this); }
    public void synchronize() { tcpState.synchronize(this); }
    public void send() { tcpState.send(this); }

    public void processOctet(TCPOctetStream stream) {}
    public void changeState(TCPState tcpState) { this.tcpState = tcpState; }
    public void printCurrentTcpStateName() { System.out.println(tcpState.getClass().getSimpleName()); }
    public void getTcpState() { System.out.println(tcpState.toString()); }
}

public class StateModel {
    public static void main(String[] args) {
        TCPConnection connection = new TCPConnection();
        connection.activeOpen();
        connection.printCurrentTcpStateName();
        connection.close();
        connection.printCurrentTcpStateName();
        connection.send();
        connection.printCurrentTcpStateName();


        connection.changeState(new TCPListen());
        connection.getTcpState();
        connection.send();
    }
}
