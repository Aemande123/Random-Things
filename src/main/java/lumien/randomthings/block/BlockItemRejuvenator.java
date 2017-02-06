package lumien.randomthings.block;

import lumien.randomthings.lib.ISuperLubricent;
import lumien.randomthings.tileentity.TileEntityItemRejuvenator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockItemRejuvenator extends BlockContainerBase implements ISuperLubricent
{
	
	public BlockItemRejuvenator()
	{
		super("itemRejuvenator",Material.ROCK);
		
		this.setHardness(2.0F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityItemRejuvenator();
	}

}
