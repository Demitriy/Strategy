import model.*;

import java.util.*;

public final class MyStrategy implements Strategy {
    private static final double WAYPOINT_RADIUS = 100.0D;

    private static final double LOW_HP_FACTOR = 0.25D;

    /**
     * Ключевые точки для каждой линии, позволяющие упростить управление перемещением волшебника.
     * <p>
     * Если всё хорошо, двигаемся к следующей точке и атакуем противников.
     * Если осталось мало жизненной энергии, отступаем к предыдущей точке.
     */
    //private final Map<LaneType, Point2D[]> waypointsByLane = new EnumMap<>(LaneType.class);

   // private Random random;

    //private LaneType lane;
    private Point2D[] waypoints;

    @Override
    public void move(Wizard self, World world, Game game, Move move) {
        initializeStrategy(self, game);

        // Постоянно двигаемся из-стороны в сторону, чтобы по нам было сложнее попасть.
        // Считаете, что сможете придумать более эффективный алгоритм уклонения? Попробуйте! ;)
        //move.setStrafeSpeed(random.nextBoolean() ? game.getWizardStrafeSpeed() : -game.getWizardStrafeSpeed());

        // Если осталось мало жизненной энергии, отступаем к предыдущей ключевой точке на линии.
       // if (self.getLife() < self.getMaxLife() * LOW_HP_FACTOR) {
        //    goTo(getPreviousWaypoint());
        //    return;
        //}

       // LivingUnit nearestTarget = getNearestTarget();

        // Если видим противника ...
      /*  if (nearestTarget != null) {
            double distance = self.getDistanceTo(nearestTarget);

            // ... и он в пределах досягаемости наших заклинаний, ...
            if (distance <= self.getCastRange()) {
                double angle = self.getAngleTo(nearestTarget);

                // ... то поворачиваемся к цели.
                move.setTurn(angle);

                // Если цель перед нами, ...
                if (StrictMath.abs(angle) < game.getStaffSector() / 2.0D) {
                    // ... то атакуем.
                    move.setAction(ActionType.MAGIC_MISSILE);
                    move.setCastAngle(angle);
                    move.setMinCastDistance(distance - nearestTarget.getRadius() + game.getMagicMissileRadius());
                }

                return;
            }
        } */

        // Если нет других действий, просто продвигаемся вперёд.

        List<Building> targets = new ArrayList<>();
    //    targets.addAll(Arrays.asList(world.getWizards()));
   //     targets.addAll(Arrays.asList(world.getMinions()));
        targets.addAll(Arrays.asList(world.getBuildings()));

       /* LivingUnit nearestTarget = null;
        double nearestTargetDistance = Double.MAX_VALUE;

        boolean enemy = false;
        boolean ally = false;
        int count = 0;
        for (LivingUnit target : targets) {
            if (belongTo(target)) {
                count++;
                if ((target.getFaction() == self.getFaction()) && (self.getX() > target.getX())) {
                    ally = true;
                }
            }
            if (((target.getFaction() != self.getFaction()) && (self.getX() < target.getX()))) enemy = true;
        }
        System.out.println(count);
        if (enemy && ally) {
            goTo(getPreviousWaypoint());
            move.setSpeed(game.getWizardForwardSpeed()/5);
        } else {
            goTo(getNextWaypoint());
        } */

        List<Minion> target1 = new ArrayList<>();
        target1.addAll(Arrays.asList(world.getMinions()));
        System.out.println(target1.size());


        goTo(getNextWaypoint(self), self, move, game);
        for (Building build : targets) {
            if (build.getFaction() != self.getFaction()) {
               // System.out.println(self.getX());
               // System.out.println(self.getY());
                if (self.getDistanceTo(build) <= (build.getAttackRange() + 150.0)) {
                    //System.out.println(self.getDistanceTo(build) + " : " + (build.getAttackRange()) + " : " + build.getX() + " : " + build.getY());
                    goTo(new Point2D(self.getX() - 50, self.getY() - 50), self, move, game);
                    //move.setSpeed(-game.getWizardBackwardSpeed());
                    move.setSpeed(0);
                }
            }
        }

    }

    /**
     * Инциализируем стратегию.
     * <p>
     * Для этих целей обычно можно использовать конструктор, однако в данном случае мы хотим инициализировать генератор
     * случайных чисел значением, полученным от симулятора игры.
     */
    private void initializeStrategy(Wizard self, Game game) {
       /* if (random == null) {
            random = new Random(game.getRandomSeed());

            double mapSize = game.getMapSize();

            waypointsByLane.put(LaneType.MIDDLE, new Point2D[]{
                    new Point2D(100.0D, mapSize - 100.0D),
                    random.nextBoolean()
                            ? new Point2D(600.0D, mapSize - 200.0D)
                            : new Point2D(200.0D, mapSize - 600.0D),
                    new Point2D(800.0D, mapSize - 800.0D),
                    new Point2D(mapSize - 600.0D, 600.0D)
            });

            waypointsByLane.put(LaneType.TOP, new Point2D[]{
                    new Point2D(100.0D, mapSize - 100.0D),
                    new Point2D(100.0D, mapSize - 400.0D),
                    new Point2D(200.0D, mapSize - 800.0D),
                    new Point2D(200.0D, mapSize * 0.75D),
                    new Point2D(200.0D, mapSize * 0.5D),
                    new Point2D(200.0D, mapSize * 0.25D),
                    new Point2D(200.0D, 200.0D),
                    new Point2D(mapSize * 0.25D, 200.0D),
                    new Point2D(mapSize * 0.5D, 200.0D),
                    new Point2D(mapSize * 0.75D, 200.0D),
                    new Point2D(mapSize - 200.0D, 200.0D)
            });

            waypointsByLane.put(LaneType.BOTTOM, new Point2D[]{
                    new Point2D(100.0D, mapSize - 100.0D),
                    new Point2D(400.0D, mapSize - 100.0D),
                    new Point2D(800.0D, mapSize - 200.0D),
                    new Point2D(mapSize * 0.25D, mapSize - 200.0D),
                    new Point2D(mapSize * 0.5D, mapSize - 200.0D),
                    new Point2D(mapSize * 0.75D, mapSize - 200.0D),
                    new Point2D(mapSize - 200.0D, mapSize - 200.0D),
                    new Point2D(mapSize - 200.0D, mapSize * 0.75D),
                    new Point2D(mapSize - 200.0D, mapSize * 0.5D),
                    new Point2D(mapSize - 200.0D, mapSize * 0.25D),
                    new Point2D(mapSize - 200.0D, 200.0D)
            });

            switch ((int) self.getId()) {
                case 3:
                case 2:
                case 6:
                case 7:
                    lane = LaneType.TOP;
                    break;
                case 1:
                case 8:
                    lane = LaneType.MIDDLE;
                    break;
                case 4:
                case 5:
                case 9:
                case 10:
                    lane = LaneType.BOTTOM;
                    break;
                default:
            }

            waypoints = waypointsByLane.get(lane);

            // Наша стратегия исходит из предположения, что заданные нами ключевые точки упорядочены по убыванию
            // дальности до последней ключевой точки. Сейчас проверка этого факта отключена, однако вы можете
            // написать свою проверку, если решите изменить координаты ключевых точек.

            /*Point2D lastWaypoint = waypoints[waypoints.length - 1];

            Preconditions.checkState(ArrayUtils.isSorted(waypoints, (waypointA, waypointB) -> Double.compare(
                    waypointB.getDistanceTo(lastWaypoint), waypointA.getDistanceTo(lastWaypoint)
            )));*/

       // }
        waypoints = new Point2D[] {new Point2D(300, game.getMapSize() - 600),
                                    new Point2D(600, game.getMapSize() - 600),
                                    new Point2D(900, game.getMapSize() - 900),
                                    new Point2D(1400, game.getMapSize() - 1400),
                                    new Point2D(1600, game.getMapSize() - 1600),
                                    new Point2D(1800, game.getMapSize() - 1800),
                                    new Point2D(2000, game.getMapSize() - 2000),
                                    new Point2D(2800, game.getMapSize() - 2800)};
    }


    private boolean belongTo(LivingUnit unit) {
            if (unit.getX() > 600)
                if (unit.getX() - 600 + 3200 <= unit.getY())
                    if (unit.getX() - 600 + 3400 >= unit.getY()) return true;
        return false;
    }

    /**
     * Данный метод предполагает, что все ключевые точки на линии упорядочены по уменьшению дистанции до последней
     * ключевой точки. Перебирая их по порядку, находим первую попавшуюся точку, которая находится ближе к последней
     * точке на линии, чем волшебник. Это и будет следующей ключевой точкой.
     * <p>
     * Дополнительно проверяем, не находится ли волшебник достаточно близко к какой-либо из ключевых точек. Если это
     * так, то мы сразу возвращаем следующую ключевую точку.
     */
    private Point2D getNextWaypoint(Wizard self) {
        int lastWaypointIndex = waypoints.length - 1;
        Point2D lastWaypoint = waypoints[lastWaypointIndex];

        for (int waypointIndex = 0; waypointIndex < lastWaypointIndex; ++waypointIndex) {
            Point2D waypoint = waypoints[waypointIndex];

            if (waypoint.getDistanceTo(self) <= WAYPOINT_RADIUS) {
                return waypoints[waypointIndex + 1];
            }

            if (lastWaypoint.getDistanceTo(waypoint) < lastWaypoint.getDistanceTo(self)) {
                return waypoint;
            }
        }

        return lastWaypoint;
    }

    /**
     * Действие данного метода абсолютно идентично действию метода {@code getNextWaypoint}, если перевернуть массив
     * {@code waypoints}.
     */
    private Point2D getPreviousWaypoint(Wizard self) {
        Point2D firstWaypoint = waypoints[0];

        for (int waypointIndex = waypoints.length - 1; waypointIndex > 0; --waypointIndex) {
            Point2D waypoint = waypoints[waypointIndex];

            if (waypoint.getDistanceTo(self) <= WAYPOINT_RADIUS) {
                return waypoints[waypointIndex - 1];
            }

            if (firstWaypoint.getDistanceTo(waypoint) < firstWaypoint.getDistanceTo(self)) {
                return waypoint;
            }
        }

        return firstWaypoint;
    }

    /**
     * Простейший способ перемещения волшебника.
     */
    private void goTo(Point2D point, Wizard self, Move move, Game game) {
        double angle = self.getAngleTo(point.getX(), point.getY());

        move.setTurn(angle);

        if (StrictMath.abs(angle) <= game.getWizardMaxTurnAngle()) {
            move.setSpeed(game.getWizardForwardSpeed());
        }
    }

    /**
     * Находим ближайшую цель для атаки, независимо от её типа и других характеристик.
     */


    private LivingUnit getNearestTarget(Wizard self, World world) {
        List<LivingUnit> targets = new ArrayList<>();
        targets.addAll(Arrays.asList(world.getBuildings()));
        targets.addAll(Arrays.asList(world.getWizards()));
        targets.addAll(Arrays.asList(world.getMinions()));

        LivingUnit nearestTarget = null;
        double nearestTargetDistance = Double.MAX_VALUE;

        for (LivingUnit target : targets) {
            if (target.getFaction() == Faction.NEUTRAL || target.getFaction() == self.getFaction()) {
                continue;
            }

            double distance = self.getDistanceTo(target);

            if (distance < nearestTargetDistance) {
                nearestTarget = target;
                nearestTargetDistance = distance;
            }
        }

        return nearestTarget;
    }

    /**
     * Вспомогательный класс для хранения позиций на карте.
     */
    private static final class Point2D {
        private final double x;
        private final double y;

        private Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getDistanceTo(double x, double y) {
            return StrictMath.hypot(this.x - x, this.y - y);
        }

        public double getDistanceTo(Point2D point) {
            return getDistanceTo(point.x, point.y);
        }

        public double getDistanceTo(Unit unit) {
            return getDistanceTo(unit.getX(), unit.getY());
        }
    }
}
