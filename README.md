# EvoImage
This is a fast-_ish_ java hill-climber that can approximate images with triangles!

Inspired by <a href="https://www.youtube.com/watch?v=27PYlj-qNb0" title="www.youtube.com/watch/...">this video</a> authored by the awesome <a href="https://www.youtube.com/channel/UCbfYPyITQ-7l4upoX8nvctg" title="www.youtube.com/channel/...">Two Minute Papers</a>

I welcome _all_ pull requests: new image examples, typos, bug-fixes, functionality, layout, GUI or typo corrections will for sure be accepted! :)

<hr/>


#### Contents:
1. [Examples](#Examples)
2. [Performance](#Performance)

<a name="Examples"></a>

# Examples

Here's some cool results this program can produce.

1. 60 vertices, 2 minutes
	<p align="center">
			<img src="https://user-images.githubusercontent.com/39745401/85873702-29303480-b7da-11ea-81c9-37dd6b5a3db9.png" height="350">
			<img src="https://user-images.githubusercontent.com/39745401/85871389-018b9d00-b7d7-11ea-8474-b7c8e48d8f0b.png" height="350">
	</p>
	
2. 650 vertices, 1 hour
	<p align="center">
			<img src="https://user-images.githubusercontent.com/39745401/85873706-2af9f800-b7da-11ea-8af9-fb62dc4a1c21.png" height="350">
			<img src="https://user-images.githubusercontent.com/39745401/85871903-b9b94580-b7d7-11ea-9358-ccaff1b2a329.png" height="350">
	</p>
	
<p>

<p align="center">
	<img src="https://user-images.githubusercontent.com/39745401/85873895-76140b00-b7da-11ea-8d38-4354edc70971.png" height="200" title="Windows 7 logo; about 4096 vertices, overnight">
	<img src="https://user-images.githubusercontent.com/39745401/85874940-174f9100-b7dc-11ea-89d0-f2dd7e4458ff.png" height="200" title="Jotaro vs DIO; 2048 vertices, 7 hours">
	<img src="https://user-images.githubusercontent.com/39745401/85928248-3883b080-b8b4-11ea-8f25-1fdd5a00bb9c.png" height="200" title="Overwatch logo; 300 vertices, 2 hours">
	<!--<img src="https://user-images.githubusercontent.com/39745401/85874940-174f9100-b7dc-11ea-89d0-f2dd7e4458ff.png" height="200" title="Jotaro vs DIO; 2048 vertices, 7 hours">-->
</p>

<a name="Performance"></a>

<hr/>

# Performance

I worked hard on optimising the image comparator, so it should work pretty fast (_160 iterations/s at 50 triangles_), but there's stil a large handicap:
```java
// Population.java, v1.4.4

buffer = deepCopy(members); // < Copies the whole population array each time iterating

buffer[MathUtil.random(buffer.length)].mutate(); // < Fine

g.setColor(Color.WHITE);
g.fillRect(0, 0, Polygonizer.WIDTH, Polygonizer.HEIGHT); // < Clear background,

for (Polygon p : buffer) { // < Very, very bad! The speed of java graphics just isn't enough.
	g.setColor(p.color); // < Simply removing this line triples the speed
	g.fillPolygon(new int[] {(int) p.p1.x, (int) p.p2.x, (int) p.p3.x}, new int[] {(int) p.p1.y, (int) p.p2.y, (int) p.p3.y}, 3); // < Sloooooow
}
```
I have no idea what to do here, since multithreading is not an option with the Graphics api. Hope i or somebody else will figure it out in the future.
