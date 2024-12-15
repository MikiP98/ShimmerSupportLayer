package io.github.mikip98.ssl.automation.structures;

public class SupportBlock {
    public final String blockId;
    public final byte radius;
    public final String[] blockStateRules;  // E.G. "age=7", "lit=true", "waterlogged=false"

    public SupportBlock(String blockId, byte radius, String[] blockStateRules) {
        this.blockId = blockId;
        this.radius = radius;
        this.blockStateRules = blockStateRules;
    }

    @Override
    public String toString() {
        StringBuilder blockStateRulesString = new StringBuilder();
        if (blockStateRules != null) {
            for (String blockStateRule : blockStateRules) {
                blockStateRulesString.append(blockStateRule).append(',');
            }
        } else {
            blockStateRulesString.append(',');
        }
        return blockId + ';' + radius + ';' + blockStateRulesString.substring(0, blockStateRulesString.length() - 1);
    }
}
