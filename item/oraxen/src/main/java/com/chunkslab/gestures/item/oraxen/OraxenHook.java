/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.item.oraxen;

import com.chunkslab.gestures.item.api.ItemHook;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

public class OraxenHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        if (!OraxenItems.exists(id))
            return null;
        return OraxenItems.getItemById(id).build();
    }

    @Override
    public String getId(ItemStack itemStack) {
        if (!OraxenItems.exists(itemStack))
            return null;
        return OraxenItems.getIdByItem(itemStack);
    }
}
