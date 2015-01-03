/*
 * Copyright 2014 Basis Technology Corp.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.basistech.tclre;

import java.util.Arrays;

/**
 * Immutable, sharable, color map.
 * This does not have a separate implementation of Tree, so that this can serve as the
 * base class for ColorMap. Instead, {@link #immutableColorMap(ColorMap)}
 * creates a copy.
  */
class RuntimeColorMap {
    protected final ColorMap.Tree[] tree;

    /**
     * Construct over a tree. It is the caller's responsibility to make an immutable copy.
     * @param tree
     */
    RuntimeColorMap(ColorMap.Tree[] tree) {
        this.tree = tree;
    }

    static short b0(char c) {
        return (short)(c & Constants.BYTMASK);
    }

    static short b1(char c) {
        return (short)((c >>> Constants.BYTBITS) & Constants.BYTMASK);
    }

    short getcolor(char c) {
        // take the first tree item in the map, then go down two levels.
        // why the extra level?
        return tree[0].ptrs[b1(c)].ccolor[b0(c)];
    }

    static RuntimeColorMap immutableColorMap(ColorMap cm) {
        ColorMap.Tree[] t = new ColorMap.Tree[cm.tree.length];
        for (int x = 0; x < cm.tree.length; x++) {
            t[x] = copyTree(cm.tree[x]);
        }
        return new RuntimeColorMap(t);
    }

    private static ColorMap.Tree copyTree(ColorMap.Tree tree) {
        if (tree == null) {
            return null;
        }
        ColorMap.Tree t = new ColorMap.Tree();
        t.ccolor = Arrays.copyOf(tree.ccolor, tree.ccolor.length);
        t.ptrs = new ColorMap.Tree[tree.ptrs.length];
        for (int x = 0; x < t.ptrs.length; x++) {
            if (tree.ptrs[x] != null) {
                t.ptrs[x] = copyTree(tree.ptrs[x]);
            }
        }
        return t;
    }
}
