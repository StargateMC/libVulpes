package zmaster587.libVulpes.tile;

import zmaster587.libVulpes.interfaces.ILinkableTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityPointer extends TileEntity implements IMultiblock, ILinkableTile {
	int masterX, masterY, masterZ;

	public TileEntityPointer() {
		super();
		masterX = 0;
		masterY = -1;
		masterZ = 0;
	}

	public TileEntityPointer(int x, int y, int z) {
		super();
		masterX = x;
		masterY = y;
		masterZ = z;
	}

	public void onLinkStart(ItemStack item, TileEntity entity, EntityPlayer player, World world) {
		if(isComplete()) {
			TileEntity master = this.getMasterBlock();
			if(master instanceof ILinkableTile) {
				((ILinkableTile)master).onLinkStart(item, master, player, world);
			}
		}
	}
	public void onLinkComplete(ItemStack item, TileEntity entity, EntityPlayer player, World world) {
		if(isComplete()) {
			TileEntity master = this.getMasterBlock();
			if(master instanceof ILinkableTile) {
				((ILinkableTile)master).onLinkComplete(item, master, player, world);
			}
		}
	}

	public boolean isSet() { return masterY != -1;}

	public int getX() {return masterX;}
	public int getY() {return masterY;}
	public int getZ() {return masterZ;}

	public void setX(int x) {masterX = x;}
	public void setY(int y) {masterY = y;}
	public void setZ(int z) {masterZ = z;}

	public void setMasterBlock(int x, int y, int z) {
		masterX = x;
		masterY = y;
		masterZ = z;
	}
	
	public TileEntity getFinalPointedTile() {
		TileEntity pointedTile = this.worldObj.getTileEntity(masterX, masterY, masterZ);
		if(pointedTile == null)
			return null;
		else if(pointedTile instanceof TileEntityPointer)
			return ((TileEntityPointer)pointedTile).getFinalPointedTile();
		else
			return pointedTile;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound comp = new NBTTagCompound();
		
		writeToNBTHelper(comp);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, comp);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		
		if(this.worldObj.isRemote)
		{
			readFromNBTHelper(pkt.func_148857_g());
		}
	}


	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return masterY != -1;
	}

	@Override
	public TileEntity getMasterBlock() {
		// TODO Auto-generated method stub
		return this.worldObj.getTileEntity(masterX, masterY, masterZ);
	}

	public boolean canUpdate() {return false;}

	@Override
	public void setComplete(int x, int y, int z) {
		this.masterX = x;
		this.masterY = y;
		this.masterZ = z;

	}

	@Override
	public void setIncomplete() {
		this.masterX = -1;
		this.masterY = -1;
		this.masterZ = -1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		writeToNBTHelper(nbtTagCompound);
	}

	private void writeToNBTHelper(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setInteger("masterX", masterX);
		nbtTagCompound.setInteger("masterY", masterY);
		nbtTagCompound.setInteger("masterZ", masterZ);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		readFromNBTHelper(nbtTagCompound);
	}

	private void readFromNBTHelper(NBTTagCompound nbtTagCompound) {
		masterX = nbtTagCompound.getInteger("masterX");
		masterY = nbtTagCompound.getInteger("masterY");
		masterZ = nbtTagCompound.getInteger("masterZ");
	}
}
