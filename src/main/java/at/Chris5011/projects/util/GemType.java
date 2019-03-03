package at.Chris5011.projects.util;

public enum GemType {
    NORMAL{
        @Override
        public String toString(){
            return "Normal Gem";
        }
    },
    SPEED{
        @Override
        public String toString(){
            return "Speedgem";
        }
    },
    FREEZE{
        @Override
        public String toString(){
            return "Freezegem";
        }
    },
    UNWRAP{
        @Override
        public String toString(){
            return "Unwrapping border Gem";
        }
    }

}
