export default function Cell({ value, isActive, onClick, onHover }) {
  const className = [
    'SB',
    isActive ? 'activeCell' : '',
  ].join(' ');
  
  return (
    <td
      className={className}
      onClick={onClick}
      onMouseOver={onHover}
    >
      {value}
    </td>
  );
}
