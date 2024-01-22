package io.github.mikip98;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import io.netty.util.internal.SuppressJava6Requirement;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static net.fabricmc.loader.api.FabricLoader.getInstance;
import static net.minecraft.server.command.CommandManager.literal;

//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.block.Blocks;
//import net.minecraft.
//import net.minecraft.world.item.alchemy.Potions; // or use a more specific import based on your needs


public class ShimmerSupportLayer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "shimmer-support-layer";
	public static final String MOD_NAME = "Shimmer Support Layer";
	public static final String MOD_CAMEL = "ShimmerSupportLayer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_CAMEL);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

//		LOGGER.info(MOD_NAME + " is initializing!");
	}
}