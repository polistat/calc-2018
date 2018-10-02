public interface PollAverager {

    double getAverage(Poll[] polls);

    double getStdv(Poll[] polls);
}
