import { DotsAndBoxesWebPage } from './app.po';

describe('dots-and-boxes-web App', function() {
  let page: DotsAndBoxesWebPage;

  beforeEach(() => {
    page = new DotsAndBoxesWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
