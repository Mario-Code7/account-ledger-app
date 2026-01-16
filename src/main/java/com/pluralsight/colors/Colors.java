package com.pluralsight.colors;

    public class Colors { // Reset
        public static final String RESET = "\033[0m";

        // Regular Colors
        public static final String BLACK = "\033[0;30m";
        public static final String RED = "\033[0;31m";
        public static final String GREEN = "\033[0;32m";
        public static final String YELLOW = "\033[0;33m";
        public static final String BLUE = "\033[0;34m";
        public static final String MAGENTA = "\033[0;35m";
        public static final String CYAN = "\033[0;36m";
        public static final String WHITE = "\033[0;37m";

        // Bold
        public static final String BOLD = "\033[1m";

        // Bright/Vibrant Colors (High Intensity)
        public static final String BRIGHT_BLACK = "\033[0;90m";
        public static final String BRIGHT_RED = "\033[0;91m";
        public static final String BRIGHT_GREEN = "\033[0;92m";
        public static final String BRIGHT_YELLOW = "\033[0;93m";
        public static final String BRIGHT_BLUE = "\033[0;94m";
        public static final String BRIGHT_MAGENTA = "\033[0;95m";
        public static final String BRIGHT_CYAN = "\033[0;96m";
        public static final String BRIGHT_WHITE = "\033[0;97m";

        // Bold + Bright (Extra Vibrant)
        public static final String BOLD_BRIGHT_RED = "\033[1;91m";
        public static final String BOLD_BRIGHT_GREEN = "\033[1;92m";
        public static final String BOLD_BRIGHT_YELLOW = "\033[1;93m";
        public static final String BOLD_BRIGHT_BLUE = "\033[1;94m";
        public static final String BOLD_BRIGHT_MAGENTA = "\033[1;95m";
        public static final String BOLD_BRIGHT_CYAN = "\033[1;96m";
        public static final String BOLD_BRIGHT_WHITE = "\033[1;97m";

        // Background Colors (Vibrant)
        public static final String BG_BRIGHT_RED = "\033[101m";
        public static final String BG_BRIGHT_GREEN = "\033[102m";
        public static final String BG_BRIGHT_YELLOW = "\033[103m";
        public static final String BG_BRIGHT_BLUE = "\033[104m";
        public static final String BG_BRIGHT_MAGENTA = "\033[105m";
        public static final String BG_BRIGHT_CYAN = "\033[106m";
        public static final String BG_BRIGHT_WHITE = "\033[107m";

        // Special Effects
        public static final String UNDERLINE = "\033[4m";
        public static final String BLINK = "\033[5m";
        public static final String REVERSE = "\033[7m";

        // RGB Colors (True Color - 256 color support)
        // Format: \033[38;5;COLORNUMBERm
        public static final String ORANGE = "\033[38;5;208m";
        public static final String PINK = "\033[38;5;205m";
        public static final String HOT_PINK = "\033[38;5;198m";
        public static final String LIME = "\033[38;5;118m";
        public static final String AQUA = "\033[38;5;51m";
        public static final String GOLD = "\033[38;5;220m";
        public static final String PURPLE = "\033[38;5;93m";
        public static final String NEON_GREEN = "\033[38;5;46m";
        public static final String NEON_BLUE = "\033[38;5;33m";

    }
