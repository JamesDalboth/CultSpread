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
    } else if (upgrade == Upgrade.MARTYR) {
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
    return true;
  }

  private static boolean matyrUpgrade(Node node) {
    node.matyr();
    World.REWARD_TOKENS += 300;
    return true;
  }

  private static boolean bombUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 200) {
      node.bomb();
      World.REWARD_TOKENS -= 200;
      return true;
    } else {
      return false;
    }
  }

  private static boolean rewardsUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setSpecialty(Specialty.CASH);
      World.REWARD_TOKENS -= 50;
      return true;
    } else {
      return false;
    }
  }

  private static boolean infidelityUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setSpecialty(Specialty.PRISONER);
      World.REWARD_TOKENS -= 50;
      return true;
    } else {
      return false;
    }
  }

  private static boolean charismaUpgrade(Node node) {
    if (World.REWARD_TOKENS >= 50) {
      node.setSpecialty(Specialty.PRIEST);
      World.REWARD_TOKENS -= 50;
      return true;
    } else {
      return false;
    }
  }

}
