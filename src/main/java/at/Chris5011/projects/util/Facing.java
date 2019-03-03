package at.Chris5011.projects.util;

public enum Facing {
    UP {
        @Override
        public String toString() {
            return "Facing Up";
        }
    }, DOWN {
        @Override
        public String toString() {
            return "Facing Down";
        }
    }, LEFT {
        @Override
        public String toString() {
            return "Facing Left";
        }
    }, RIGHT {
        @Override
        public String toString() {
            return "Facing Right";
        }
    }
}
