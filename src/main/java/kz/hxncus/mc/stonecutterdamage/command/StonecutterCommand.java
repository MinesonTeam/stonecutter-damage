/*
 * MIT License
 *
 * Copyright (c) Hxncus <hxncusgaming@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package kz.hxncus.mc.stonecutterdamage.command;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import kz.hxncus.mc.stonecutterdamage.config.Config;
import lombok.NonNull;
import org.bukkit.command.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StonecutterCommand part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterCommand implements TabExecutor {

    private final StonecutterDamage plugin;
    private final Config config;

    public StonecutterCommand(StonecutterDamage plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            for (String message : config.getHelpMessages()) {
                sender.sendMessage(message.replace("{0}", label));
            }
            return true;
        } else if ("reload".equalsIgnoreCase(args[0])) {
            config.reload();
            sender.sendMessage(config.getReloadMessage());
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String @NonNull [] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "reload");
        }
        return Collections.emptyList();
    }
}