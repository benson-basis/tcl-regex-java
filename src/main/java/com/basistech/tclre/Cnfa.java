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

/**
 * Compacted (runtime) NFA.
 */
class Cnfa {
    static final int HASLACONS = 1;
    final int ncolors;        /* number of colors */
    final int flags;
    final int pre;        /* setup state number */
    final int post;       /* teardown state number */
    final short[] bos;     /* colors, if any, assigned to BOS and BOL */
    final short[] eos;     /* colors, if any, assigned to EOS and EOL */
    final long[] arcs;
    // each state is an index of an arc.
    final int[] states;


    Cnfa(int ncolors, int flags, int pre, int post, short[] bos, short[] eos, long[] arcs, int[] states) {
        this.ncolors = ncolors;
        this.flags = flags;
        this.pre = pre;
        this.post = post;
        this.bos = bos;
        this.eos = eos;
        this.arcs = arcs;
        this.states = states;
    }

    void dump() {
        System.out.format("nstates %d\nncolors %d\nflags %x\npre %d\npost %d\nbos [%d, %d]\neos [%d %d]\n",
            states.length,
            ncolors,
            flags,
            pre,
            post,
            bos[0],
            bos[1],
            eos[0],
            eos[1]);
        System.out.format("    color state\n");
        for (int x = 0; x < arcs.length; x++) {
            long arc = arcs[x];
            System.out.format("%03d %5d %d\n", x, carcColor(arc), carcTarget(arc));
        }
        System.out.println();
    }

    static long packCarc(short color, int targetState) {
        return ((long)color << 32) | targetState;
    }

    static short carcColor(long packed) {
        return (short)(packed >>> 32);
    }

    static int carcTarget(long packed) {
        return (int)packed;
    }




}
