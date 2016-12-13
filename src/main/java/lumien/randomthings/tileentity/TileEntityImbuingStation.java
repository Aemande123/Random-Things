package lumien.randomthings.tileentity;

import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityImbuingStation extends TileEntityBase implements IInventory, ITickable
{
	InventoryBasic inventory;
	public int imbuingProgress;

	ItemStack currentOutput = ItemStack.field_190927_a;

	public final static int IMBUING_LENGTH = 200;

	public TileEntityImbuingStation()
	{
		inventory = new InventoryBasic("Imbuing Station", false, 5);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("imbuingProgress", imbuingProgress);
		InventoryUtil.writeInventoryToCompound(nbt, inventory);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound nbt)
	{
		this.imbuingProgress = nbt.getInteger("imbuingProgress");
		InventoryUtil.readInventoryFromCompound(nbt, inventory);
		this.currentOutput = ImbuingRecipeHandler.getRecipeOutput(inventory);
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			ItemStack validOutput = ImbuingRecipeHandler.getRecipeOutput(inventory);
			if (!ItemStack.areItemStacksEqual(validOutput, currentOutput) && canHandleOutput(validOutput))
			{
				this.imbuingProgress = 0;
				currentOutput = validOutput;
			}

			
			if (!this.currentOutput.func_190926_b())
			{
				this.imbuingProgress++;
				if (this.imbuingProgress >= IMBUING_LENGTH)
				{
					imbuingProgress = 0;
					imbue();
				}
			}
			else
			{
				this.imbuingProgress = 0;
			}
		}
	}

	private boolean canHandleOutput(ItemStack validOutput)
	{
		if (validOutput.func_190926_b() || inventory.getStackInSlot(4).func_190926_b())
		{
			return true;
		}
		else
		{
			ItemStack currentInOutput = inventory.getStackInSlot(4);
			ItemStack requiredOutput = validOutput;

			if (!(ItemStack.areItemsEqual(currentInOutput, requiredOutput) && ItemStack.areItemStackTagsEqual(currentInOutput, requiredOutput)))
			{
				return false;
			}
			else
			{
				if (currentInOutput.func_190916_E() + requiredOutput.func_190916_E() > currentInOutput.getMaxStackSize())
				{
					return false;
				}
			}
		}
		return true;
	}

	private void imbue()
	{
		// Set Output
		if (this.inventory.getStackInSlot(4).func_190926_b())
		{
			this.inventory.setInventorySlotContents(4, currentOutput.copy());
		}
		else
		{
			this.inventory.getStackInSlot(4).func_190917_f(currentOutput.func_190916_E());
		}

		// Decrease Ingredients
		for (int slot = 0; slot < this.inventory.getSizeInventory() - 1; slot++)
		{
			this.inventory.decrStackSize(slot, 1);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return inventory.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		return inventory.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index != 4;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
	}

	@Override
	public String getName()
	{
		return "Imbuing Station";
	}

	@Override
	public boolean func_191420_l()
	{
		return this.inventory.func_191420_l();
	}
}
