package fr.badblock.game.v1_8_R3.merchant;

import fr.badblock.game.v1_8_R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

public class FakeMerchant implements IMerchant {
	private BadblockPlayer 	   player;
	private EntityHuman        tradingPlayer;
	private MerchantRecipeList offers;
	private TranslatableString displayName;

	public FakeMerchant(BadblockPlayer player, MerchantRecipeList offers, TranslatableString name){
		this.player		   = player;
		this.offers 	   = offers;
		this.displayName   = name;
		
		this.tradingPlayer = ((GameBadblockPlayer) player).getHandle();
	}

	@Override
	public void a_(EntityHuman entityhuman){
		this.tradingPlayer = entityhuman;
	}

	@Override
	public EntityHuman v_(){
		return this.tradingPlayer;
	}

	@Override
	public void a(MerchantRecipe merchantrecipe){

	}
	
	@Override
	public void a_(ItemStack itemstack){

	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman entityhuman) {
		return this.offers;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName(){
		return toChat(displayName.getAsLine(player));
	}
	
	private IChatBaseComponent toChat(String base){
		return ChatSerializer.a("{\"text\": \"" + base + "\"}");
	}
}
