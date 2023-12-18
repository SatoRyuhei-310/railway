/**
 * 
 */
package railway;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RailWaySystem {

	static double longestDistance = 0;
	static List<Integer> longestRoute = new ArrayList<>();

	static Map<Integer, Station> stations = new HashMap<>();
	static Map<String, Route> routes = new HashMap<>();

	/**
	 * RouteとStationのインスタンスを生成し、各Mapに追加
	 * @param start 経路の始点
	 * @param end 経路の終点
	 * @param distance 経路の距離
	 */
	static void addRoute(int start, int end, double distance) {
		if (stations.get(start) != null) {
			stations.get(start).to.add(end);
		} else {
			stations.put(start, new Station(start));
			stations.get(start).to.add(end);
		}
		stations.putIfAbsent(end, new Station(end));

		String routeName = Integer.toString(start) + "to" + Integer.toString(end);
		routes.putIfAbsent(routeName, new Route(routeName, distance));
	}

	/**
	 * 最長経路を求める処理 endStationから向かえる点があれば再帰処理
	 * @param stations 駅のMap
	 * @param startStation 経路の始点となる駅
	 * @param endStation 経路の終点となる駅
	 * @param currentDistance 現在の距離
	 * @param currentRoute 現在の経路
	 */
	static void findLongestRoute(Map<Integer, Station> stations, Station startStation, Station endStation,
			double currentDistance,
			List<Integer> currentRoute) {

		List<Integer> addedRoute = new ArrayList<Integer>(currentRoute);
		addedRoute.add(endStation.id);

		double addedDistance = currentDistance
				+ routes.get(Integer.toString(startStation.id) + "to" + Integer.toString(endStation.id)).distance;

		if (addedDistance > longestDistance) {
			longestDistance = addedDistance;
			longestRoute = addedRoute;
		}

		//一度訪れた点に再度訪れた場合終了
		if (addedRoute.stream().filter(i -> i == endStation.id).count() > 1) {
			return;
		}

		for (int next : endStation.to) {

			findLongestRoute(stations, endStation, stations.get(next), addedDistance, addedRoute);
		}

	}

	public static void main(String[] args) {

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("routeMap.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			String[] tokens = line.split("\\s*,\\s*");

			if (tokens.length == 3) {
				int start = Integer.parseInt(tokens[0]);
				int end = Integer.parseInt(tokens[1]);
				double distance = Double.parseDouble(tokens[2]);

				RailWaySystem.addRoute(start, end, distance);
			}
		}

		for (Map.Entry<Integer, Station> station : stations.entrySet()) {
			Station startStation = station.getValue();
			List<Integer> currentRoute = new ArrayList<>();
			currentRoute.add(station.getValue().id);

			for (int next : startStation.to) {

				findLongestRoute(stations, startStation, stations.get(next), 0, currentRoute);

			}

		}

		for (int station : longestRoute) {
			System.out.println(station);
		}
	}
}
