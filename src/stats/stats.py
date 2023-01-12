from matplotlib import pyplot as plt
from matplotlib import lines
import re

#
# I SHOULD MAKE A PLOT OF RUNTIME (WITH 2 BARS NEXT TO EACH OTHER PER DAY)
#

with open('stats.txt', 'r') as file:
    stats = list(file.readlines())

times = []

for stat in stats:
    day = int(stat[:4].strip())
    time1, time2 = re.findall("[0-9]{2}:[0-9]{2}:[0-9]{2}", stat)
    h1, m1, s1 = map(int, time1.split(":"))
    h2, m2, s2 = map(int, time2.split(":"))
    # Add all up in seconds
    total1 = s1 + 60 * m1 + 60 * 60 * h1
    total2 = s2 + 60 * m2 + 60 * 60 * h2
    # Convert to minutes for easier reading
    times.append((day, total1 // 60, total2 // 60))

times.sort()

# Plot
x = range(1, len(times) + 1)

fig, ax = plt.subplots(facecolor='#0f0f23')

ax.bar(x, [t[1] for t in times], color='#9999cc', zorder=20)
ax.bar(x, [t[2] for t in times], color='#ffff66', zorder=10)

ax.set_title('Time elapsed since 6 AM', color='#cccccc')
ax.set_yticks(range(0, 361, 60))
ax.legend(frameon=False, loc='upper left', handles=[
    lines.Line2D([], [], color='#ffff66', marker='*', markersize=10, linestyle='None', label='Part 2'),
    lines.Line2D([], [], color='#9999cc', marker='*', markersize=10, linestyle='None', label='Part 1')])
for text in ax.get_legend().get_texts():
    text.set_color('#009900')
ax.set_facecolor('#0f0f23')
ax.tick_params(left=False, bottom=False, labelleft=False, labelbottom=False)
[ax.spines[side].set_visible(False) for side in ax.spines]
ax.grid(color='#cccccc', linewidth=0.5, axis='y', alpha=0.4, zorder=0)

# Time in minutes, each line a 60m mark
plt.text(0.35, 0.4, 'damn fishes', transform=ax.transAxes, fontsize=8, color='#cccccc')
plt.text(0.8, 0.7, 'twisted logic', transform=ax.transAxes, fontsize=8, color='#cccccc')

plt.show()

# fig.savefig('codetime.png', dpi=300)
# fig.savefig('runtime.png', dpi=300)
