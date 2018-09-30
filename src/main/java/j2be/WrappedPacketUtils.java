package j2be;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class WrappedPacketUtils {
	
	public static int readUnsignedVarInt(ByteBuf buf) {
		buf.readerIndex(0);
        int value = 0;
        int i = 0;
        int b;
        while (((b = buf.readByte()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            Preconditions.checkArgument(i <= 35, "Variable length quantity is too long (must be <= 35)");
        }
        return value | (b << i);
    }
	
		public static ByteBuf prependLength(ByteBuf inputPacket) {
			ByteBuf outputPacket = Unpooled.buffer();
			int packetLength = inputPacket.writerIndex();
			while ((packetLength & 0xFFFFFF80) != 0L) {
	            outputPacket.writeByte((packetLength & 0x7F) | 0x80);
	            packetLength >>>= 7;
	        }
	        outputPacket.writeByte(packetLength & 0x7F);
	        outputPacket.writeBytes(inputPacket);
	        return outputPacket;
	    }
			
			
		
	
	public static ByteBuf singlePacketWrap(ByteBuf inputPacket) {
		ByteBuf outputPacket = Unpooled.buffer();
		outputPacket.writeByte(0xfe);
		outputPacket.writeBytes(inputPacket);
		return outputPacket;
		
		
	}
	

}