import Cell from './Cell';

export default function BigBoard({
  r, c,
  board,
  bigStatus,
  isActive,
  onCellClick,
  onHover,
}) {
  if (bigStatus && bigStatus !== 'D') {
    return <td className="BB wonBoard">{bigStatus}</td>;
  }
  if (bigStatus === 'D') {
    return <td className="BB drawBoard"></td>;
  }
  return (
    <td className={`BB ${isActive ? 'activeBoard' : ''}`}>
      <table className="MB">
        <tbody>
          {[0,1,2].map(rr => (
            <tr key={rr}>
              {[0,1,2].map(cc => (
                <Cell
                  key={cc}
                  value={board[rr][cc]}
                  isActive={isActive}
                  onHover={onHover}
                  onClick={() => isActive && !board[rr][cc] && onCellClick(rr, cc)}
                />
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </td>
  );
}
