public class Upgrader {

  public static boolean attemptUpgrade(Node node, Upgrade upgrade) {
    if (node.getStatus() != Cult.BLUE) {
      if (upgrade == Upgrade.CONVERT){
        return convertUpgrade(node);
      }
      return false;
    }

    if (upgrade == Upgrade.CHARISMA) {
      return charismaUpgrade(node);
    } else if (upgrade == Upgrade.INFIDELITY) {
      return infidelityUpgrade(node);
    } else if (upgrade == Upgrade.REWARDS) {
      return rewardsUpgrade(node);
    } else if (upgrade == Upgrade.BOMB) {
      return bombUpgrade(node);
    } else if (upgrade == Upgrade.MATYR) {
      return matyrUpgrade(node);
    } else {
      return false;
    }
  }

  private static boolean convertUpgrade(Node node) {
    if (node.getStatus() != Cult.NEUTRAL || World.CONVERSION_TOKENS == 0) {
      return false;
    }

    World.CONVERSION_TOKENS--;

    node.setNextStatus(Cult.BLUE);
    node.setStatus(Cult.BLUE);

    World.redrawLabel();
    return true;
  }

  private static boolean matyrUpgrade(Node node) {
    node.matyr();
    World.REWARD_TOKENS += 300;
    World.redrawLabel();
    return true;
  }

  private static boolean bombUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 200) {
      node.bomb();
      World.REWARD_TOKENS -= 200;
      World.redrawLabel();
      return true;
    } else {
      return false;
    }
  }

  private static boolean rewardsUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setRewardsRate(node.getRewardsRate() + 2);
      World.REWARD_TOKENS -= 50;
      World.redrawLabel();
      return true;
    } else {
      return false;
    }
  }

  private static boolean infidelityUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setInfidelity(node.getInfidelity()- 0.3);
      World.REWARD_TOKENS -= 50;
      World.redrawLabel();
      return true;
    } else {
      return false;
    }
  }

  private static boolean charismaUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setCharisma(node.getCharisma() + 0.1);
      World.REWARD_TOKENS -= 50;
      World.redrawLabel();
      return true;
    } else {
      return false;
    }
  }

}
