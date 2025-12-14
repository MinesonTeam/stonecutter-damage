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

package kz.hxncus.mc.stonecutterdamage.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Map;

/**
 * StonecutterEntities part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterEntities {
    private final Map<LivingEntity, BlockVector> entityBlockCache = new HashMap<>();

    public BlockVector get(LivingEntity entity) {
        return entityBlockCache.get(entity);
    }

    public BlockVector put(LivingEntity entity, BlockVector vector) {
        return entityBlockCache.put(entity, vector);
    }

    public BlockVector remove(LivingEntity entity) {
        return entityBlockCache.remove(entity);
    }
}
