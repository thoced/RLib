package com.ss.rlib.network.server.impl;

import com.ss.rlib.concurrent.GroupThreadFactory;
import com.ss.rlib.network.NetworkConfig;
import com.ss.rlib.network.impl.AbstractAsynchronousNetwork;
import com.ss.rlib.network.server.AcceptHandler;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * The base implementation of a server network.
 *
 * @author JavaSaBr
 */
public final class DefaultServerNetwork extends AbstractAsynchronousNetwork implements ServerNetwork {

    /**
     * The asynchronous channel group.
     */
    @NotNull
    private final AsynchronousChannelGroup group;

    /**
     * The asynchronous server socket channel.
     */
    @NotNull
    private final AsynchronousServerSocketChannel channel;

    /**
     * The accept handler.
     */
    @NotNull
    private final AcceptHandler acceptHandler;

    /**
     * Instantiates a new Default server network.
     *
     * @param config        the config
     * @param acceptHandler the accept handler
     * @throws IOException the io exception
     */
    public DefaultServerNetwork(@NotNull final NetworkConfig config, @NotNull final AcceptHandler acceptHandler)
            throws IOException {
        super(config);

        this.group = AsynchronousChannelGroup.withFixedThreadPool(config.getGroupSize(),
                new GroupThreadFactory(config.getGroupName(), config.getThreadClass(), config.getThreadPriority()));

        this.channel = AsynchronousServerSocketChannel.open(group);
        this.acceptHandler = acceptHandler;
    }

    @Override
    public <A> void accept(@Nullable final A attachment, @NotNull final CompletionHandler<AsynchronousSocketChannel, ? super A> handler) {
        channel.accept(attachment, handler);
    }

    @Override
    public void bind(@NotNull final SocketAddress address) throws IOException {
        channel.bind(address);
        channel.accept(channel, acceptHandler);
    }

    @Override
    public String toString() {
        return "DefaultServerNetwork{" +
                "group=" + group +
                ", channel=" + channel +
                ", acceptHandler=" + acceptHandler +
                "} " + super.toString();
    }
}
