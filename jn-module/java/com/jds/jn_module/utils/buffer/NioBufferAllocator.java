package com.jds.jn_module.utils.buffer;

import java.nio.ByteBuffer;

/**
 * Allocates {@link jds.mina.core.buffer.IoBuffer}s and manages them.  Please implement this
 * interface if you need more advanced memory management scheme.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public interface NioBufferAllocator
{
	/**
	 * Returns the buffer which is capable of the specified size.
	 *
	 * @param capacity the capacity of the buffer
	 * @param direct   <tt>true</tt> to get a direct buffer,
	 *                 <tt>false</tt> to get a heap buffer.
	 */
	NioBuffer allocate(int capacity, boolean direct);

	/**
	 * Returns the NIO buffer which is capable of the specified size.
	 *
	 * @param capacity the capacity of the buffer
	 * @param direct   <tt>true</tt> to get a direct buffer,
	 *                 <tt>false</tt> to get a heap buffer.
	 */
	ByteBuffer allocateNioBuffer(int capacity, boolean direct);

	/**
	 * Wraps the specified NIO {@link java.nio.ByteBuffer} into MINA buffer.
	 */
	NioBuffer wrap(ByteBuffer nioBuffer);

	/**
	 * Dispose of this allocator.
	 */
	void dispose();
}