package com.ibtsoft.singularity.web.socketio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoWebSocket;
import io.socket.parseqs.ParseQS;

public final class EngineIoEndpoint extends Endpoint {

    private Session mSession;
    private Map<String, String> mQuery;
    private EngineIoWebSocket mEngineIoWebSocket;

    private EngineIoServer mEngineIoServer; // The engine.io server instance

    @Override
    public void onOpen(final Session session, final EndpointConfig endpointConfig) {
        mSession = session;
        mQuery = ParseQS.decode(session.getQueryString());

        mEngineIoWebSocket = new EngineIoWebSocketImpl();

        /*
         * These cannot be converted to lambda because of runtime type inference
         * by server.
         */
        mSession.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(final String message) {
                mEngineIoWebSocket.emit("message", message);
            }
        });
        mSession.addMessageHandler(new MessageHandler.Whole<byte[]>() {
            @Override
            public void onMessage(final byte[] message) {
                mEngineIoWebSocket.emit("message", (Object) message);
            }
        });

        mEngineIoServer.handleWebSocket(mEngineIoWebSocket);
    }

    @Override
    public void onClose(final Session session, final CloseReason closeReason) {
        super.onClose(session, closeReason);

        mEngineIoWebSocket.emit("close");
        mSession = null;
    }

    @Override
    public void onError(final Session session, final Throwable thr) {
        super.onError(session, thr);

        mEngineIoWebSocket.emit("error", "unknown error", thr.getMessage());
    }

    private class EngineIoWebSocketImpl extends EngineIoWebSocket {

        private RemoteEndpoint.Basic mBasic;

        EngineIoWebSocketImpl() {
            mBasic = mSession.getBasicRemote();
        }

        @Override
        public Map<String, String> getQuery() {
            return mQuery;
        }

        @Override
        public Map<String, List<String>> getConnectionHeaders() {
            return null;
        }

        @Override
        public void write(final String message) throws IOException {
            mBasic.sendText(message);
        }

        @Override
        public void write(final byte[] message) throws IOException {
            mBasic.sendBinary(ByteBuffer.wrap(message));
        }

        @Override
        public void close() {
            try {
                mSession.close();
            } catch (IOException ignore) {
                throw new RuntimeException(ignore);
            }
        }
    }
}
