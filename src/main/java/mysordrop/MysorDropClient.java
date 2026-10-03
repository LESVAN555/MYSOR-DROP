package mysordrop;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class MysorDropClient implements ClientModInitializer {
  static final SoundEvent AD = SoundEvent.of(new Identifier("mysordrop", "ad"));
  static KeyBinding key;
  static int showTicks = 0;

  public void onInitializeClient() {
    key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.mysordrop.ad", InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_R, "category.mysordrop"));

    ClientTickEvents.END_CLIENT_TICK.register(c -> {
      while (key.wasPressed()) {
        c.getSoundManager().play(PositionedSoundInstance.master(AD, 1f));
        showTicks = 100;
      }
      if (showTicks > 0) showTicks--;
    });

    HudRenderCallback.EVENT.register((ctx, delta) -> {
      if (showTicks <= 0) return;
      MinecraftClient mc = MinecraftClient.getInstance();
      int w = mc.getWindow().getScaledWidth();
      int h = mc.getWindow().getScaledHeight();
      MatrixStack m = ctx.getMatrices();
      m.push();
      m.translate(w / 2f, h / 3f, 0);
      m.scale(4f, 4f, 1f);
      ctx.drawCenteredTextWithShadow(mc.textRenderer, "MYSOR DROP", 0, 0, 0xFFFF55);
      m.pop();
    });
  }
}
