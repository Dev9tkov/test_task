import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ilya Devyatkov
 * @since 09.07.2020
 */
public class Task implements Navigator {

    /**
     * список "стен"
     */
    private List<Vertex> wallList = new ArrayList<>();

    /**
     * посещенные вершины
     */
    private List<Vertex> checked = new ArrayList<>();

    /**
     * Начальная вершина
     */
    private Vertex start;

    /**
     * Конечная вершина
     */
    private Vertex end;

    /**
     * кол-во строк
     */
    private int width;

    /**
     * кол-во столбцов
     */
    private int high;
    private Queue<Vertex> queue = new LinkedList<>();
    private boolean haveWay = false;
    /**
     * Мапа соответствия вершина "родитель"-вершина "потомок"
     */
    private Map<Vertex, Vertex> parents = new LinkedHashMap<>();


    /**
     * Метод парсинга "карты" на начальную, конечную точку, а так
     * же "стен"
     */
    private void fillingData(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            width = i + 1;
            for (int j = 0; j < map[i].length; j++) {
                high = j + 1;
                if (map[i][j] == '@') {
                    start = new Vertex(i, j);
                } else if (map[i][j] == 'X') {
                    end = new Vertex(i, j);
                } else if (map[i][j] == '#') {
                    wallList.add(new Vertex(i, j));
                }
            }
        }
    }

    /**
     * Метод для нахождения соседей вершины
     */
    private List<Vertex> neighbors(Vertex vertex) {
        List<Vertex> rsl;
        //(x+1, y), (x, y-1), (x-1, y), (x, y+1)
        Vertex neighborUp = new Vertex(vertex.x - 1, vertex.y);
        Vertex neighborDown = new Vertex(vertex.x + 1, vertex.y);
        Vertex neighborLeft = new Vertex(vertex.x, vertex.y - 1);
        Vertex neighborRight = new Vertex(vertex.x, vertex.y + 1);
        rsl = List.of(neighborUp, neighborDown, neighborLeft, neighborRight).stream()
                .filter(val -> inBounds(val.x, val.y))//отсечем лишние вершины, которые выходят за рамки "карты"
                .filter(val -> !wallList.contains(val))//отсечем лишние вершины, которые являются "стеной"
                .collect(Collectors.toList());
        return rsl;
    }

    /**
     * Существует ли вершина в округе заднной вершины
     * и не выходит ли она за границы заданной матрицы
     */
    private boolean inBounds(int x, int y) {
        boolean rsl = false;
        if ((0 <= x && x < width) && (0 <= y && y < high)) {
            rsl = true;
        }
        return rsl;
    }

    /**
     * Основной метод поиска в ширину
     * @param start начальная вершина
     */
    private void bsf(Vertex start) {
        checked.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            List<Vertex> list = neighbors(current);
            for (Vertex val : list) {
                if (haveWay) {break;}
                if (val.equals(end)) {
                    haveWay = true;
                    parents.put(val, current);
                    break;
                }
                if (!checked.contains(val)) {
                    checked.add(val);
                    queue.add(val);
                    parents.put(val, current);
                }
            }
        }
    }

    /**
     * Вспомогательный метод для разворачивания LinkedHashMap.
     * @param map карта из конца в начало
     * @return карта из начала в конец
     */
    private Map<Vertex, Vertex> reverse(Map<Vertex, Vertex> map) {
        LinkedHashMap<Vertex, Vertex> reversedMap = new LinkedHashMap<>();
        ListIterator<Map.Entry<Vertex, Vertex>> it = new ArrayList<>(map.entrySet()).listIterator(map.entrySet().size());

        while (it.hasPrevious()) {
            Map.Entry<Vertex, Vertex> el = it.previous();
            reversedMap.put(el.getKey(), el.getValue());
        }
        return reversedMap;
    }

    /**
     * Нахождение пути
     * @param map карта родителей
     * @return лист вершин, которые составляют путь
     */
    private List<Vertex> pathToStart(Map<Vertex, Vertex> map) {
        List<Vertex> rsl = new ArrayList<>();
        Vertex temp = end;
        for (Map.Entry<Vertex, Vertex> item : map.entrySet()) {
            if (temp.equals(item.getKey())) {
                rsl.add(item.getKey());
                temp = item.getValue();
            }
        }
        rsl.remove(end);
        return rsl;
    }

    /**
     * Метод прокладывает путь в новой карте
     * @param maps изначальная карта
     * @param path короткий путь
     * @return новая карта с путем из началной точки в конечную
     */
    private char[][] draw(char[][] maps, List<Vertex> path) {
        char[][] rsl = new char[width][high];
        for(int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                if (path.contains(new Vertex(i, j)) && maps[i][j] != 'X') {
                    rsl[i][j] = '+';
                } else {
                    rsl[i][j] = maps[i][j];
                }
            }
        }
        return rsl;
    }

    /**
     * Основной метод, который собирает все предыдущие
     * @param map начальная карта
     * @return
     */
    @Override
    public char[][] searchRoute(char[][] map) {
        fillingData(map);
        bsf(start);
        if (!haveWay) {
            return null;
        }
        Map<Vertex, Vertex> parentsChilds = reverse(parents);
        List<Vertex> path = pathToStart(parentsChilds);
        return draw(map, path);
    }

    /**
     * Класс - вершина. Точка на карте с координатами
     * Поля сделал публичными просто для удобства, чтобы не добавлять
     * гетеры
     */
    private class Vertex {
        public int x;
        public int y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return x == vertex.x &&
                    y == vertex.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}

