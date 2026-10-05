package com.example.client;

import com.example.client.network.SlideClientNetworking;
import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SlideClientNetworking.initClient();
	}
}