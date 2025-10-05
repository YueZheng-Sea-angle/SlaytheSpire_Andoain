package Andoain.helpers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;
import java.util.List;

public class PetHelper {
    private static final List<AbstractMonster> pets = new ArrayList<>();

    public static void addPet(AbstractPlayer player, AbstractMonster pet) {
        pets.add(pet);
        // 这里可以添加更多初始化逻辑
    }

    public static void removePet(AbstractPlayer player, AbstractMonster pet) {
        pets.remove(pet);
    }

    public static List<AbstractMonster> getPets(AbstractPlayer player) {
        return new ArrayList<>(pets);
    }

    public static void clearPets() {
        pets.clear();
    }
}