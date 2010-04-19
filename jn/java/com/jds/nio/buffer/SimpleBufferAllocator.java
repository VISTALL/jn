package com.jds.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * A simplistic {@link jds.mina.core.buffer.IoBufferAllocator} which simply allocates a new
 * buffer every time.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class SimpleBufferAllocator implements NioBufferAllocator
{

	public NioBuffer allocate(int capacity, boolean direct)
	{
		return wrap(allocateNioBuffer(capacity, direct));
	}

	public ByteBuffer allocateNioBuffer(int capacity, boolean direct)
	{
		ByteBuffer nioBuffer;
		if (direct)
		{
			nioBuffer = ByteBuffer.allocateDirect(capacity);
		}
		else
		{
			nioBuffer = ByteBuffer.allocate(capacity);
		}
		return nioBuffer;
	}

	public NioBuffer wrap(ByteBuffer nioBuffer)
	{
		return new SimpleBuffer(nioBuffer);
	}

	public void dispose()
	{
		// Do nothing
	}

	private class SimpleBuffer extends AbstractNioBuffer
	{
		private ByteBuffer buf;

		protected SimpleBuffer(ByteBuffer buf)
		{
			super(SimpleBufferAllocator.this, buf.capacity());
			this.buf = buf;
			buf.order(ByteOrder.BIG_ENDIAN);
		}

		protected SimpleBuffer(SimpleBuffer parent, ByteBuffer buf)
		{
			super(parent);
			this.buf = buf;
		}

		@Override
		public ByteBuffer buf()
		{
			return buf;
		}

		@Override
		protected void buf(ByteBuffer buf)
		{
			this.buf = buf;
		}

		@Override
		protected NioBuffer duplicate0()
		{
			return new SimpleBuffer(this, this.buf.duplicate());
		}

		@Override
		protected NioBuffer slice0()
		{
			return new SimpleBuffer(this, this.buf.slice());
		}

		@Override
		protected NioBuffer asReadOnlyBuffer0()
		{
			return new SimpleBuffer(this, this.buf.asReadOnlyBuffer());
		}

		@Override
		public byte[] array()
		{
			return buf.array();
		}

		@Override
		public int arrayOffset()
		{
			return buf.arrayOffset();
		}

		@Override
		public boolean hasArray()
		{
			return buf.hasArray();
		}

		@Override
		public void free()
		{
			// Do nothing
		}
	}
}