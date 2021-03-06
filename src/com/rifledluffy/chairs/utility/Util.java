package com.rifledluffy.chairs.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import com.rifledluffy.chairs.RFChairs;
import com.rifledluffy.chairs.chairs.CarpetBlock;
import com.rifledluffy.chairs.chairs.Chair;
import com.rifledluffy.chairs.chairs.SlabBlock;
import com.rifledluffy.chairs.chairs.StairBlock;
import com.rifledluffy.chairs.config.ConfigManager;

public class Util {
	
	private static RFChairs plugin = RFChairs.getPlugin(RFChairs.class);
	private static ConfigManager configManager = plugin.getConfigManager();

	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static void debug(String message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(message);
	}
	
	public static void debug(int message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Integer.toString(message));
	}
	
	public static void debug(Double message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Double.toString(message));
	}
	
	public static void debug(Boolean message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) player.sendMessage(Boolean.toString(message));
	}

	public static void debug(Object message) {
		debug(message.toString());
	}
	
	public static boolean isStairBlock(Material material) {
		if (StairBlock.from(material) == "null") return false;
		return true;
	}
	
	public static String replaceMessage(Player player, String string) {
		string = ChatColor.translateAlternateColorCodes('&',string);
		string = string.replace("%user%", player.getName());
		return string;
	}
	
	public static String replaceMessage(Entity entity, Player target, String string) {
		if (entity instanceof LivingEntity) return replaceMessage((LivingEntity)entity, target, string);
		
		string = ChatColor.translateAlternateColorCodes('&',string);
		string = string.replace("%user%", entity.getName());
		string = string.replace("%seated%", target.getName());
		return string;
	}
	
	public static String replaceMessage(LivingEntity entity, Player target, String string) {
		if (entity instanceof Player)  return replaceMessage((Player)entity, target, string);
			
		string = ChatColor.translateAlternateColorCodes('&',string);
		string = string.replace("%user%", entity.getName());
		string = string.replace("%seated%", target.getName());
		return string;
	}
	
	public static String replaceMessage(Player player, Player target, String string) {
		string = ChatColor.translateAlternateColorCodes('&',string);
		string = string.replace("%user%", player.getName());
		string = string.replace("%seated%", target.getName());
		return string;
	}
	
	public static boolean isSlabBlock(Material material) {
		if (SlabBlock.from(material) == "null") return false;
		return true;
	}
	
	public static boolean isCarpetBlock(Material material) {
		if (CarpetBlock.from(material) == "null") return false;
		return true;
	}
	
	public static boolean validateStair(Block block) {
		List<String> whitelist = plugin.getConfig().getStringList("allowed-chairs");
		List<String> blacklist = plugin.getConfig().getStringList("blacklisted-chairs");
		boolean found = false;
		for (String white : whitelist) if (white.equalsIgnoreCase(StairBlock.from(block.getType()))) found = true;
		for (String black : blacklist) if (black.equalsIgnoreCase(StairBlock.from(block.getType()))) found = false;
		if (!found) return false;
		Stairs stair = (Stairs) block.getState().getBlockData();
		if (stair.getHalf() == Bisected.Half.TOP) return false;
		return true;
	}
	
	public static boolean validateSlab(Block block) {
		List<String> whitelist = plugin.getConfig().getStringList("allowed-chairs");
		List<String> blacklist = plugin.getConfig().getStringList("blacklisted-chairs");
		boolean found = false;
		for (String white : whitelist) if (white.equalsIgnoreCase(SlabBlock.from(block.getType()))) found = true;
		for (String black : blacklist) if (black.equalsIgnoreCase(SlabBlock.from(block.getType()))) found = false;
		if (!found) return false;
		Slab slab = (Slab) block.getState().getBlockData();
		if (slab.getType() != Slab.Type.BOTTOM) return false;
		return true;
	}

	public static boolean validateCarpet(Block block) {
		List<String> whitelist = plugin.getConfig().getStringList("allowed-chairs");
		List<String> blacklist = plugin.getConfig().getStringList("blacklisted-chairs");
		boolean found = false;
		for (String white : whitelist) if (white.equalsIgnoreCase(CarpetBlock.from(block.getType()))) found = true;
		for (String black : blacklist) if (black.equalsIgnoreCase(CarpetBlock.from(block.getType()))) found = false;
		if (!found) return false;
		return true;
	}
	
	public static boolean validatedChair(Block block) {
		if (isStairBlock(block.getType())) return validateStair(block);
		return false;
	}
	
	public static boolean playerIsSeated(UUID uuid, Map<UUID, Chair> chairMap) {
		Chair chair = chairMap.get(uuid);
		return chair != null;
	}
	
	public static boolean nearLiquid(Block block) {
		return isLiqiudOrMagma(block.getRelative(BlockFace.NORTH))
				|| isLiqiudOrMagma(block.getRelative(BlockFace.EAST))
				|| isLiqiudOrMagma(block.getRelative(BlockFace.SOUTH))
				|| isLiqiudOrMagma(block.getRelative(BlockFace.WEST));
	}
	
	public static boolean surroundedBlock(Block block) {
		return !validExit(block.getRelative(BlockFace.NORTH))
				&& !validExit(block.getRelative(BlockFace.EAST))
				&& !validExit(block.getRelative(BlockFace.SOUTH))
				&& !validExit(block.getRelative(BlockFace.WEST));
	}
	
	public static boolean validExit(Block block) {
		return block.getType() == Material.AIR || block.getType() == Material.WALL_SIGN;
	}
	
	public static boolean isLiqiudOrMagma(Block block) {
		return block.getBlockData() instanceof Levelled || block.getRelative(BlockFace.DOWN).getType() == Material.MAGMA_BLOCK;
	}
	
	public static boolean validCouch(Block block) {
		int validSides = 0;
		if (!validatedChair(block)) return false;
		Stairs stair = (Stairs) block.getState().getBlockData();
		
		Set<BlockFace> blockFaces = stair.getFaces();
		List<BlockFace> faces = new ArrayList<BlockFace>(blockFaces);
		faces.remove(stair.getFacing());
		faces.remove(stair.getFacing().getOppositeFace());
		faces.remove(BlockFace.DOWN);
		faces.remove(BlockFace.UP);
		
		BlockFace side = faces.get(0);
		BlockFace otherSide = faces.get(1);
		
		if (validatedChair(block.getRelative(side))) if (validSeat(block.getRelative(side), side, block)) validSides++;
		if (validatedChair(block.getRelative(otherSide))) if (validSeat(block.getRelative(otherSide), otherSide, block)) validSides++;
		
		if (!validatedChair(block.getRelative(side))) if (validSeat(block, side, block)) validSides++;
		if (!validatedChair(block.getRelative(otherSide))) if (validSeat(block, otherSide, block)) validSides++;
		return validSides == 2;
	}
	
	public static boolean validSeat(Block block, BlockFace side, Block original) {
		Stairs stair = (Stairs) block.getState().getBlockData();
		Stairs first = (Stairs) original.getState().getBlockData();
		if (stair.getFacing() != first.getFacing()) return false;
		if (block.getRelative(side).getType() == Material.WALL_SIGN) {
			WallSign sign =((WallSign)block.getRelative(side).getBlockData());
			return sign.getFacing() == side;
		}
		else if (validatedChair(block.getRelative(side))) return validSeat(block.getRelative(side), side, original);
		return false;
	}
	
	public static boolean sameSeat(Player player, Block block, Map<UUID, Chair> chairMap) {
		Chair chair = chairMap.get(player.getUniqueId());
		if (chair == null) return false;
		return chair.getLocation().equals(block.getLocation());
	}
	
	public static boolean samePosition(Block block, Block target) {
		return block.getX() == target.getX() && block.getY() == target.getY() && block.getZ() == target.getZ();
	}

	public static boolean throneChair(Block block) {
		if (!(block.getBlockData() instanceof Stairs)) return false;
		if (block.getRelative(BlockFace.UP).getBlockData() instanceof TrapDoor) {
			TrapDoor trapDoor = (TrapDoor) block.getRelative(BlockFace.UP).getBlockData();
			if (!trapDoor.isOpen()) return false;
			return trapDoor.getFacing().getOppositeFace() == ((Stairs) block.getBlockData()).getFacing();
		}
		return false;
	}
	
	public static boolean blockIsChair(Block block, List<Chair> chairs) {
		for (Chair chair: chairs) {
			if (chair == null) continue;
			if (chair.getLocation() == null) continue;
			if (samePosition(block, chair.getBlock())) return true;
		}
		return false;
	}
	
	public static Chair getChairFromBlock(Block block, List<Chair> chairs) {
		for (Chair chair : chairs) {
			if (chair == null) continue;
			if (chair.getLocation() == null) continue;
			if (samePosition(chair.getBlock(), block)) return chair;
		}
		return null;
	}
	
	public static ArmorStand generateFakeSeat(Chair chair) {
		if (chair == null) return null;
		Location seat = chair.getLocation();
		BlockFace facing = null;
		if (Util.isStairBlock(chair.getBlock().getType())) facing = ((Stairs)chair.getBlock().getState().getBlockData()).getFacing();
		Location playerLoc = chair.getPlayer().getEyeLocation();
		playerLoc.setPitch(0);
		Vector vector;
		if (facing != null) vector = getVectorFromFace(chair.getBlock(), facing.getOppositeFace());
		else vector = getVectorFromNearBlock(chair.getBlock(),playerLoc.getBlock());
		
		Double height = 0.25D;
		if (Util.isCarpetBlock(chair.getBlock().getType())) height = 0D;

                ArmorStand armorStand = (ArmorStand) seat.getWorld().spawn(seat.add(0.5, height, 0.5).setDirection(vector), ArmorStand.class, stand -> {
                        stand.setVisible(false);
                        stand.setGravity(false);
                        stand.setInvulnerable(true);
                        stand.setMarker(true);
                        stand.setCollidable(false);
                    });
		
		return armorStand;
	}
	
	public static float getAbsoluteAngle(Location loc) {
		float y = loc.getYaw();
        if (y < 0) y += 360;
        y %= 360;
        if (y <= 45 || y >= 315) return 0;
        if (y >= 45 && y <= 135) return 90;
        if (y >= 135 && y <= 225) return 180;
		return 270;
	}

	public static String getCardinalDirection(Location loc) {
		float y = loc.getYaw();
        if (y < 0) y += 360;
        y %= 360;
        if (y <= 45 || y >= 315) return "south";
        if (y >= 45 && y <= 135) return "west";
        if (y >= 135 && y <= 225) return "north";
		return "east";
	}
	
	public static Block getBlockFromDirection(Block block, String direction) {
		if (direction.equalsIgnoreCase("north")) return block.getRelative(BlockFace.NORTH);
		else if (direction.equalsIgnoreCase("west")) return block.getRelative(BlockFace.WEST);
		else if (direction.equalsIgnoreCase("east")) return block.getRelative(BlockFace.EAST);
		else if (direction.equalsIgnoreCase("south")) return block.getRelative(BlockFace.SOUTH);
		else if (direction.equalsIgnoreCase("down")) return block.getRelative(BlockFace.DOWN);
		else if (direction.equalsIgnoreCase("up")) return block.getRelative(BlockFace.UP);
		return block;
	}
	
	public static boolean canFitPlayer(Block block) {
		return block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() == Material.AIR;
	}
	
	public static boolean safePlace(Block block) {
		return block.getRelative(BlockFace.DOWN).getType() != Material.AIR;
	}
	
	public static Vector getVectorDir(Location caster, Location target) {
		return target.clone().subtract(caster.toVector()).toVector();
	}
	
	public static Vector getVectorFromFace(Block block, BlockFace face) {
		Location blockLoc = block.getLocation();
		Location faceLoc = block.getRelative(face).getLocation();
		return getVectorDir(blockLoc, faceLoc);
	}
	
	public static Vector getVectorFromNearBlock(Block block, Block target) {
		return target.getLocation().toVector().subtract(block.getLocation().toVector());
	}
	
	public static void callEvent(Event event) {
		plugin.getServer().getPluginManager().callEvent(event);
	}

}
