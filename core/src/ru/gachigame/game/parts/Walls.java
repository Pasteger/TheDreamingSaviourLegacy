package ru.gachigame.game.parts;

import com.badlogic.gdx.utils.Array;

public class Walls {
    Wall wall1;
    Wall wall2;
    Wall wall3;
    Wall wall4;
    Wall wall5;
    Wall wall6;
    Wall wall7;
    Wall wall8;
    Wall wall9;
    Wall wall10;
    Wall wall11;
    Wall wall12;
    Wall wall13;
    Wall wall14;
    Wall wall15;
    Wall wall16;
    Wall wall17;
    Wall wall18;
    Wall wall19;
    Wall wall20;
    Wall wall21;
    Wall wall22;
    Wall wall23;
    Wall wall24;
    Wall wall25;
    Wall wall26;
    Wall wall27;
    Wall wall28;

    public Array<Wall> wallsArray = new Array<>();

    public Walls() {
        //коридор спавна слева и пол комнаты 1 слева
        wall1 = new Wall(673, 0, 229, 342);

        //коридор спавна справа и пол комнаты 1 справа
        wall2 = new Wall(932, 0, 300, 342);

        //снизу
        wall3 = new Wall(0, -100, 2000, 100);

        //комната 1 стена слева
        wall4 = new Wall(680, 340, 100, 404);

        //комната 1 стена справа
        wall5 = new Wall(1068, 342, 100, 342);

        //комната 1 потолок слева
        wall6 = new Wall(780, 600, 125, 144);

        //коридор 2 стена слева
        wall7 = new Wall(867, 600, 40, 537);

        //коридор 2 стена справа
        wall8 = new Wall(943, 600, 735, 260);

        //коридор 2 потолок
        wall9 = new Wall(907, 889, 412, 40);

        //коридор 2 стена справа 2
        wall10 = new Wall(1352, 858, 380, 72);

        //комната 2 стена слева
        wall11 = new Wall(867,928, 233, 209);


        //столб в комнате 2 1:1
        wall12 = new Wall(1166, 1000, 67, 67);

        //столб в комнате 2 1:2
        wall13 = new Wall(1300, 1000, 67, 67);

        //столб в комнате 2 1:3
        wall14 = new Wall(1434, 1000, 67, 67);

        //столб в комнате 2 2:1
        wall15 = new Wall(1166, 1134, 67, 67);

        //столб в комнате 2 2:2
        wall16 = new Wall(1300, 1134, 67, 67);

        //столб в комнате 2 2:3
        wall17 = new Wall(1434, 1134, 67, 67);

        //столб в комнате 2 2:1
        wall18 = new Wall(1166, 1268, 67, 67);

        //столб в комнате 2 2:2
        wall19 = new Wall(1300, 1268, 67, 67);

        //столб в комнате 2 2:3
        wall20 = new Wall(1434, 1268, 67, 67);


        //комната 2 стена справа
        wall21 = new Wall(1573, 929, 100, 580);

        //комната 2 потолок
        wall22 = new Wall(1100, 1402, 473,100);

        //комната 3 стена справа
        wall23 = new Wall(867, 1203, 233, 528);

        //комната 3 потолок
        wall24 = new Wall(315, 1580, 553, 100);

        //комната 3 стена слева
        wall25 = new Wall(215, 744, 100, 936);

        //комната 3 пол
        wall26 = new Wall(215, 644, 650, 100);


        //комната 3 столб 1
        wall27 = new Wall(500, 926, 183,183);

        //комната 3 столб 2
        wall28 = new Wall(500, 1292, 183,183);

        wallsArray.add(wall1, wall2, wall3, wall4);
        wallsArray.add(wall5, wall6, wall7, wall8);
        wallsArray.add(wall9, wall10, wall11, wall12);
        wallsArray.add(wall13, wall14, wall15, wall16);
        wallsArray.add(wall17, wall18, wall19, wall20);
        wallsArray.add(wall21, wall22, wall23, wall24);
        wallsArray.add(wall25, wall26, wall27, wall28);
    }
}
