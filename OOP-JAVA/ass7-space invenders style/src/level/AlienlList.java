package level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import animations.GameLevel;
import biuoop.DrawSurface;
import primitives.Velocity;
import sprite.Sprite;
/**
 * @author YanaPatyuk
 * alien list is group of aliens.
 */
public class AlienlList implements Sprite {
    private List<Alien> aliens;
    private List<Alien> lastRow;
    private Velocity correntVelocity;
    private Velocity fisrtVelocity;
    private GameLevel game;
    private double shootTime;
    private boolean loser;
    private boolean winner;
    /**
     * Contractor.
     * @param level game.
     */
    public AlienlList(GameLevel level) {
        this.aliens = new ArrayList<Alien>();
        this.lastRow = new ArrayList<Alien>();
        this.loser = false;
        this.winner = false;
        this.game = level;
    }
    /**
     * set velocity for all the aliens.
     * @param v velocity.
     */
    public void setVelocity(Velocity v) {
        this.fisrtVelocity = v;
        this.correntVelocity = v;
    }
    /**
     * increase speed by 10%.
     */
    public void increaseSpeed() {
        this.correntVelocity = this.correntVelocity.fixedVelocity(1.1);
    }
    /**
     * add new alien to list.
     * @param e alien.
     */
    public void addElien(Alien e) {
        this.aliens.add(e);
    }
    /**
     * add new alien to last row.
     * @param e alien.
     */
    public void addToLastRow(Alien e) {
        this.lastRow.add(e);
    }
    /**
     * increase the velocity when gamer end the level.
     */
    public void increaseSpeedEndLevel() {
        this.correntVelocity = this.correntVelocity.fixedVelocity(1.2);
    }
    @Override
    public void drawOn(DrawSurface d) {
    }
    @Override
    public void timePassed(double dt) {
        //decreese time from last shoot of alien by dt.
        this.shootTime = this.shootTime - dt;
        //check if ther is aliens. of not stop.
        if (this.aliens.isEmpty()) {
            this.winner = true;
            return;
        }
        //check if its shoot time.
        if (this.shootTime < 0) {
            this.game.shoutABall(getRandomAlien().getCollisionRectangle());
            this.shootTime = 1;
        }
        //check if lowest alien hit the shild. if does-lose.
        if (getLowestAlien().getCollisionRectangle().getLowerRight().getY() >= 450) {
            this.loser = true;
        }
        boolean moveDown = false;
        //check if hitted right or left frame. if hitted-move down.
        if (getRightestAlien().getCollisionRectangle().getLowerRight().getX() >= 800) {
               this.correntVelocity.changeDx();
               moveDown = true;
               increaseSpeed();
        } else if (getLefteAlien().getCollisionRectangle().getUpperLeft().getX() <= 10) {
            this.correntVelocity.changeDx();
            increaseSpeed();
            moveDown = true;
        }
        Velocity fixedVelocity = this.correntVelocity.fixedVelocity(dt);
        for (int i = 0; i < this.aliens.size(); i++) {
            if (moveDown) {
                this.aliens.get(i).changePlace(new Velocity(0, 10));
        }
            //move all aliens.
            this.aliens.get(i).changePlace(fixedVelocity);
    }
    }
/**
 * @return rightest alien.
 */
    private Alien getRightestAlien() {
        Alien r = this.aliens.get(0);
        for (int i = 1; i < this.aliens.size(); i++) {
            if (r.getCollisionRectangle().getUpperLeft().getX()
                   < this.aliens.get(i).getCollisionRectangle().getUpperLeft().getX()) {
                r = this.aliens.get(i);
            }
        }
        return r;
    }
    /**
     * @return leftest alien.
     */
    private Alien getLefteAlien() {
        Alien r = this.aliens.get(0);
        for (int i = 1; i < this.aliens.size(); i++) {
            if (r.getCollisionRectangle().getUpperLeft().getX()
                    >=  this.aliens.get(i).getCollisionRectangle().getUpperLeft().getX()) {
                r = this.aliens.get(i);
            }
        }
        return r;
    }
    /**
     * @return lowest alien.
     */
    private Alien getLowestAlien() {
        Alien r = this.aliens.get(0);
        for (int i = 1; i < this.aliens.size(); i++) {
            if (r.getCollisionRectangle().getLowerRight().getY()
                 <=   this.aliens.get(i).getCollisionRectangle().getLowerRight().getY()) {
                r = this.aliens.get(i);
            }
        }
        return r;
    }
    /**
     * @return  random alien in lowest row.
     */
    private Alien getRandomAlien() {
        Random random = new Random();
        Alien a = this.lastRow.get(random.nextInt(this.lastRow.size()));
        for (int i = 0; i < this.aliens.size(); i++) {
            if (a.getCollisionRectangle().getUpperLeft().getX()
                    == this.aliens.get(i).getCollisionRectangle().getUpperLeft().getX()) {
                if (a.getCollisionRectangle().getUpperLeft().getY()
                        < this.aliens.get(i).getCollisionRectangle().getUpperLeft().getY()) {
                    a = this.aliens.get(i);
                }
            }
        }
        return a;
    }

/**
 * remove alien from the lists.
 * @param a to remove.
 */
    public void removeAlien(Alien a) {
        this.aliens.remove(a);
        if (this.lastRow.contains(a)) {
            this.lastRow.remove(a);
        }
        if (this.aliens.isEmpty()) {
            this.winner = true;
        }
    }
/**
 * @return if lose the game.
 */
    public boolean checkIfLost() {
        return this.loser;
    }
    /**
     * @return if player won.
     */
    public boolean checkIfWin() {
        return this.winner;
    }
    /**
     * remove all aliens.
     */
    public void clearList() {
        this.aliens.clear();
        this.lastRow.clear();
    }
    /**
     * @return list of aliens.
     */
    public List<Alien> getList() {
        List<Alien> copy = new ArrayList<Alien>();
        for (int i = 0; i < this.aliens.size(); i++) {
            copy.add(this.aliens.get(i).copy());
        }
        return copy;
    }
    /**
     * reaset aliens to ateat point and change vlocity.
     */
    public void reastar() {
        this.loser = false;
        this.winner = false;
        this.correntVelocity = this.fisrtVelocity.fixedVelocity(1.4);
        this.fisrtVelocity = this.fisrtVelocity.fixedVelocity(1.4);
        clearList();
    }
}
