package jingwood.tank.element.tank;

import jingwood.tank.WinMain;
import jingwood.tank.battleground.BattleGround;
import jingwood.tank.element.Bullet;
import jingwood.tank.element.MoveableElement;
import jingwood.tank.element.obstacle.Obstructive;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

public class Tank extends Thread implements MoveableElement,
        Obstructive {

    private int tankid;
    public static final int PRIMARY_TANK = 1;            // Primary player
    public static final int SECONDARY_TANK = 2;          // Secondary player
    public static final int TARGET_NORMAL_TANK = 5;      // Enemy normal tank
    public static final int TARGET_SHOT_TANK = 6;        // Enemy slow shot tank
    public static final int TARGET_FAST_TANK = 7;        // Enemy fast shot tank
    public static final int TARGET_PERFECT_TANK = 8;     // Enemy heavy armored tank

    private int direction;
    private int homedir;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;

    private Image[][][] images = new Image[4][4][3];
    private int step;
    private int destination;
    private Point location = new Point();
    private Point homeplace = new Point();
    private int unit = 16;
    private Vector ob;
    private boolean isPlayer = false;
    private boolean isRich = false;
    private boolean riching = false;
    private int life = 1;
    private int obdurability = 1;          // How many shot to destroy (enemy tank only)
    private int score = 0;
    private int value = 0;                 // Score to be get when player killed this tank (enemy tank only)
    private boolean isMoveAble = false;

    private int shotcountable = 1;         // maximum number of shots in a same time
    private Bullet[] shots = new Bullet[2];    // Shooting bullet array

    private int status;
    public static final int ST_WAITING = 0;
    public static final int ST_NAISSANCING = 1;
    public static final int ST_NORMAL = 2;
    public static final int ST_DEAD = 3;
    public static final int ST_DISABLE = 4;

    private int naissance = 0;
    private Image[] naissances = new Image[4];

    private float protecter = 0;
    private Image[] protecters = new Image[4];

    private boolean weedable = false;

    public static final int LV_SINGLE = 0;
    public static final int LV_FAST = 1;
    public static final int LV_DOUBLE = 2;
    public static final int LV_PERFECT = 3;
    private int level = LV_SINGLE;

    private int blast = 0;
    private Image[] blasts = new Image[3];

    public BattleGround bg;
    private int isDisplayValue = 1;

    public Tank(int tankid, int x, int y, int direction, BattleGround bg) {
        this.bg = bg;
        this.location.x = this.unit * x * 2;
        this.location.y = this.unit * y * 2;
        this.homeplace.x = this.location.x;
        this.homeplace.y = this.location.y;
        this.direction = direction;
        this.homedir = direction;
        this.tankid = tankid;

        switch (tankid) {
            case PRIMARY_TANK:
            case SECONDARY_TANK:
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        for (int i = 0; i < 3; i++) {
                            images[j][k][i] = WinMain.tk.getImage("images/tank" + tankid + (j + 1) + (k + 1) + (i + 1) + ".gif");
                        }
                    }
                }
                this.isPlayer = true;
                this.life = 3;
                this.status = ST_DISABLE;
                break;
            case TARGET_NORMAL_TANK:
            case TARGET_SHOT_TANK:
            case TARGET_FAST_TANK:
            case TARGET_PERFECT_TANK:
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        for (int i = 0; i < 3; i++) {
                            images[j][k][i] = WinMain.tk.getImage("images/tank" + tankid + (j + 1) + (k + 1) + (i + 1) + ".gif");
                        }
                    }
                }
                this.isPlayer = false;
                this.status = Tank.ST_WAITING;
                switch (tankid) {
                    case TARGET_FAST_TANK:
                        value = 100;
                        break;
                    case TARGET_PERFECT_TANK:
                        value = 200;
                        break;
                    default:
                        value = 50;
                        break;
                }
                break;
        }
        for (int i = 0; i < this.naissances.length; i++) {
            naissances[i] = WinMain.tk.getImage("images/naissance" + (i + 1) + ".gif");
        }
        for (int i = 0; i < this.protecters.length; i++) {
            this.protecters[i] = WinMain.tk.getImage("images/protecter" + (i + 1) + ".gif");
        }
        for (int i = 0; i < this.blasts.length; i++) {
            this.blasts[i] = WinMain.tk.getImage("images/bigblast" + (i + 1) + ".gif");
        }
        this.start();
    }

    public void run() {
        while (true) {
            if (this.bg.isPaused) continue;

            if (this.isDisplayValue > 0) this.isDisplayValue--;
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null && shots[i].getStatus() == Bullet.ST_DISABLE) shots[i] = null;
            }
            if (this.protecter > 0) this.protecter--;
            if (this.isRich) {
                this.riching = !this.riching;
            }

            if (isPlayer) {
                this.isMoveAble = this.bg.getStatus() == BattleGround.ST_NORMAL
                        || this.bg.getStatus() == BattleGround.ST_WINNING;
            } else {
                this.isMoveAble = this.bg.getStatus() != BattleGround.ST_STATISTICING;
            }

            switch (this.status) {
                case ST_NAISSANCING:
                    this.naissancing();
                    break;
                case ST_NORMAL:
                    if (!this.isPlayer) this.thinking();
                    this.moving();
                    break;
                case ST_DEAD:
                    this.blasting();
                    break;
                case ST_DISABLE:
                    //if (this.isDisplayValue<=0) return;
            }

            this.pause(150);
        }
    }

    public Image getImage() {
        switch (this.status) {
            case ST_NAISSANCING:
                return this.naissances[this.naissance];
            case ST_NORMAL:
                if (this.riching) {
                    return this.images[3][this.direction - 1][this.step];
                } else {
                    return this.images[this.level][this.direction - 1][this.step];
                }
            case ST_DEAD:
                return this.blasts[this.blast];
        }
        return null;
    }

    public int getOrderLevel() {
        return 1;
    }

    public void setDestination(int value) {
        if (!isMoveAble) return;
        this.destination = value * this.unit;
    }

    public boolean turnUp() {
        if (!isMoveAble) return false;
        this.direction = UP;
        this.location.x = this.location.x - (this.location.x + 16) % 8;
        return true;
    }

    public boolean turnRight() {
        if (!isMoveAble) return false;
        this.direction = RIGHT;
        this.location.y = this.location.y - (this.location.y + 16) % 8;
        return true;
    }

    public boolean turnDown() {
        if (!isMoveAble) return false;
        this.direction = DOWN;
        this.location.x = this.location.x - (this.location.x + 16) % 8;
        return true;
    }

    public boolean turnLeft() {
        if (!isMoveAble) return false;
        this.direction = LEFT;
        this.location.y = this.location.y - (this.location.y + 16) % 8;
        return true;
    }

    public Point getLocation() {
        return this.location;
    }

    public void draw(Graphics2D comp, BattleGround bg) {
        switch (this.status) {
            case ST_NAISSANCING:
            case ST_NORMAL:
                comp.drawImage(this.getImage(), 20 + this.location.x, 10 + this.location.y, bg);
                if (this.protecter > 0) {
                    comp.drawImage(this.protecters[WinMain.rand.nextInt(this.protecters.length)],
                            20 + this.location.x, 10 + this.location.y, bg);
                }
                break;
            case ST_DEAD:
                comp.drawImage(this.getImage(), 16 + this.location.x, 6 + this.location.y, bg);
                break;
        }
        if (isDisplayValue > 0) {
            comp.setFont(new Font("Arial", 0, 11));
            comp.setColor(Color.white);
            comp.drawString(Integer.toString(value), this.location.x + 30, this.location.y + 30);
        }
    }

    public int getDirection() {
        return this.direction;
    }

    public int getDestination() {
        return this.destination;
    }

    public void setObstacles(Vector ob) {
        this.ob = ob;
    }

    public Vector getObstacles() {
        return this.ob;
    }

    public int getStatus() {
        return this.status;
    }

    public void moving() {
        stopforward:
        while (this.destination-- > 0) {
            if (this.status != Tank.ST_NORMAL) {
                return;
            }
            if (this.protecter > 0) this.protecter -= .1;
            if (this.isRich) {
                this.riching = !this.riching;
            }
            if (++this.step > 2) {
                this.step = 0;
            }
            synchronized (this.ob) {
                Iterator obs = this.ob.iterator();
                while (obs.hasNext()) {
                    Object obn = obs.next();
                    if (obn instanceof Obstructive) {
                        Obstructive ob = (Obstructive) obn;
                        if (!ob.checkMoveAble(this)) continue stopforward;
                    }
                }
            }
            switch (this.direction) {
                case UP:
                    if (this.location.y > 1) this.location.y--;
                    break;
                case RIGHT:
                    if (this.location.x < 12 * 32) this.location.x++;
                    break;
                case DOWN:
                    if (this.location.y < 12 * 32) this.location.y++;
                    break;
                case LEFT:
                    if (this.location.x > 1) this.location.x--;
                    break;
            }
            if (this.isPlayer) {
                this.pause(15);
            } else {
                this.pause(40);
            }
        }
    }

    public void pause(int value) {
        try {
            sleep(value);
        } catch (InterruptedException ignore) {
        }
    }

    public void naissancing() {
        this.addLife(-1);
        this.location.x = this.homeplace.x;
        this.location.y = this.homeplace.y;
        this.direction = this.homedir;
        for (int k = 0; k < 2; k++) {
            for (int i = this.naissance - 2; i >= 0; i--) {
                pause(50);
            }
            for (int i = 0; i < this.naissances.length; i++) {
                this.naissance = i;
                pause(50);
            }
        }
        this.status = ST_NORMAL;
        if (this.isPlayer) this.protecter = 20;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public void setProtected(int value) {
        this.protecter = value;
    }

    public void shot() {
        //if (!isMoveAble) return false;

        for (int i = 0; i < this.shotcountable && i < shots.length; i++) {
            if (this.shots[i] == null) {
                shots[i] = new Bullet(this);
                synchronized (ob) {
                    ob.addElement(shots[i]);
                }
                //return true;
            }
        }
        //return false;
    }

    public Bullet[] getShots() {
        return this.shots;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean getWeedable() {
        return this.weedable;
    }

    public void setLevel(int value) {
        if (value >= LV_SINGLE && value <= LV_PERFECT
                && getLevel() != value)
            this.level = value;

        switch (value) {
            case LV_SINGLE:
            case LV_FAST:
                this.shotcountable = 1;
                break;
            case LV_DOUBLE:
            case LV_PERFECT:
                this.shotcountable = 2;
                break;
        }
    }

    public boolean isPlayer() {
        return this.isPlayer;
    }

    private void thinking() {
        switch (WinMain.rand.nextInt(20)) {
            case 1:
                this.setDestination(2);
                turnUp();
            case 2:
            case 3:
                this.setDestination(2);
                turnDown();
                break;
            case 4:
            case 5:
                this.setDestination(2);
                turnLeft();
                break;
            case 6:
            case 7:
                this.setDestination(2);
                turnRight();
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                this.shot();
            default:
                this.setDestination(2);
        }
    }

    public boolean checkMoveAble(Tank tank) {
        if (this.status != Tank.ST_NORMAL) return true;

        int tx = tank.getLocation().x;
        int ty = tank.getLocation().y;
        int x = this.location.x;
        int y = this.location.y;

        switch (tank.getDirection()) {
            case Tank.UP:
                if (ty <= y + 32 && ty > y - 32 && tx > x - 32 && tx < x + 32) {
                    return ty < y + 32 && ty >= y - 32 && tx >= x - 32 && tx <= x + 32;
                }
                break;
            case Tank.DOWN:
                if (ty >= y - 32 && ty < y + 32 && tx > x - 32 && tx < x + 32) {
                    return ty > y - 32 && ty <= y + 32 && tx >= x - 32 && tx <= x + 32;
                }
                break;
            case Tank.LEFT:
                if (tx <= x + 32 && tx > x - 32 && ty > y - 32 && ty < y + 32) {
                    return tx < x + 32 && tx >= x - 32 && ty >= y - 32 && ty <= y + 32;
                }
                break;
            case Tank.RIGHT:
                if (tx >= x - 32 && tx < x + 32 && ty > y - 32 && ty < y + 32) {
                    return tx > x - 32 && tx <= x + 32 && ty >= y - 32 && ty <= y + 32;
                }
                break;
        }
        return true;
    }

    public void setRich() {
        if (!this.isPlayer) {
            this.isRich = true;
        }
    }

    public boolean getRich() {
        return this.isRich;
    }

    public boolean checkHitAble(Bullet shot) {
        if (this.status != Tank.ST_NORMAL) return false;

        int tx = shot.getLocation().x;
        int ty = shot.getLocation().y;
        int x = this.location.x;
        int y = this.location.y;

        switch (shot.getDirection()) {
            case Bullet.DT_UP:
                if (ty <= y + 24 && ty > y - 24 && tx > x - 16 && tx < x + 32) {
                    shot2dead(shot);
                    return true;
                }
                break;
            case Bullet.DT_DOWN:
                if (ty >= y - 8 && ty < y + 24 && tx > x - 16 && tx < x + 32) {
                    shot2dead(shot);
                    return true;
                }
                break;
            case Bullet.DT_LEFT:
                if (tx <= x + 24 && tx > x - 24 && ty > y - 16 && ty < y + 32) {
                    shot2dead(shot);
                    return true;
                }
                break;
            case Bullet.DT_RIGHT:
                if (tx >= x - 8 && tx < x + 24 && ty > y - 16 && ty < y + 32) {
                    shot2dead(shot);
                    return true;
                }
                break;

        }
        return false;
    }

    private void shot2dead(Bullet shot) {
        if (this.protecter <= 0) {
            if (this.obdurability-- <= 1) {
                if (shot.getTank().getTankID() < 3) {
                    shot.getTank().bg.stat.addStat(shot.getTank().getTankID(), this.tankid);
                }
                this.status = Tank.ST_DEAD;
                if (this.tankid >= 5) {
                    this.isDisplayValue = 4;
                    shot.getTank().addScore(value);
                }
            }
            if (this.isRich) {
                shot.setProp(true);
            }
        }
    }

    private void blasting() {
        for (int i = 0; i < this.blasts.length; i++) {
            this.blast = i;
            pause(60);
        }
        pause(100);
        if (this.life > 0) {
            this.status = Tank.ST_NAISSANCING;
        } else {
            this.status = Tank.ST_DISABLE;
        }
    }

    public int getLife() {
        return this.life;
    }

    public String getLifeString() {
        return ((getLife() >= 0 ? getLife() : 0) < 10 ? "0" : "") + this.life;
    }

    public void addLife(int value) {
        this.life += value;
    }

    public void addScore(int value) {
        this.score += value;
    }

    public int getScore() {
        return this.score;
    }

    public int getTankID() {
        return this.tankid;
    }

}
