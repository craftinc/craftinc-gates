/*  Craft Inc. Gates
    Copyright (C) 2011-2013 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates.util;

import org.bukkit.ChatColor;

import java.util.List;

public class TextUtil {

    public static String titleSize(String str) {
        String center = ".[ " + ChatColor.YELLOW + str + ChatColor.GOLD + " ].";

        if (center.length() >= 60) {
            return ChatColor.GOLD + center;
        } else {
            String line = ChatColor.GOLD + repeat("_", 60);

            int pivot = line.length() / 2;
            int eatLeft = center.length() / 2;
            int eatRight = center.length() - eatLeft;

            return line.substring(0, pivot - eatLeft) + center + line.substring(pivot + eatRight);
        }
    }


    private static String repeat(String s, int times) {
        if (times <= 0)
            return "";

        return s + repeat(s, times - 1);
    }

    /**
     * Joins all elements of list into a single string, separating the original strings with glue.
     */
    public static String implode(List<String> list, String glue) {
        if (list.size() == 0) {
            return "";
        }

        String ret = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            ret += glue + list.get(i);
        }

        return ret;
    }
}
