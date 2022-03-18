package com.raffelberg.cr_ticker;

//import androidx.test.core.app.ApplicationProvider;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /*
    private MatchDao matchDao;
    private MatchRoomDatabase matchRoomDatabase;
    MatchRepository matchRepository;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        matchRoomDatabase = Room.inMemoryDatabaseBuilder(context, MatchRoomDatabase.class).build();
        matchDao = matchRoomDatabase.matchDao();
        matchRepository = new MatchRepository(ApplicationProvider.getApplicationContext());
    }


    @Test
    public void addMatchRoom(){
        Match match1 = new Match("","test1",new Team(""),new Team(""));
        Match match2 = new Match("","test2",new Team(""),new Team(""));

        matchRepository.insertMatch(match1);
        matchRepository.getCurrentMatch().observe(ApplicationProvider.getApplicationContext(),match -> {
            assertThat(match.getTeam1().getTeam_name(),equalTo("test1"));
        });
    }

    @After
    public void closeDb() throws IOException {
        matchRoomDatabase.close();
    }

     */

}